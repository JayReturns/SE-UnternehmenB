package com.dhbw.unternehmenb.ssp.controller;

import com.dhbw.unternehmenb.ssp.auth.FirebaseAuthFilter;
import com.dhbw.unternehmenb.ssp.interfaces.ServerApi;
import com.dhbw.unternehmenb.ssp.model.*;
import com.dhbw.unternehmenb.ssp.model.response.AllUsersVEnvRequestResponseBody;
import com.dhbw.unternehmenb.ssp.model.response.AllUsersVRResponseBody;
import com.dhbw.unternehmenb.ssp.view.UserRepository;
import com.dhbw.unternehmenb.ssp.view.VacationRequestRepository;
import com.dhbw.unternehmenb.ssp.view.VirtualEnvironmentRepository;
import com.dhbw.unternehmenb.ssp.view.VirtualEnvironmentRequestRepository;
import com.dhbw.unternehmenb.ssp.model.response.ProvisioningResponse;
import com.dhbw.unternehmenb.ssp.view.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class MainServerController implements ServerApi {
    //please don't delete the unused Logger, just for quick debugging
    private final Logger logger = LoggerFactory.getLogger(MainServerController.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VacationRequestRepository vacationRequestRepository;
    @Autowired
    private VirtualEnvironmentRequestRepository virtualEnvironmentRequestRepository;
    @Autowired
    private VirtualEnvironmentRepository virtualEnvironmentRepository;
    @Autowired
    private FirebaseAuth firebaseAuth;
    @Autowired
    private FirebaseAuthFilter firebaseAuthFilter;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private ProvisioningRepository provisioningRepository;

    private User getCurrentUser() {
        String token = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findById(token).orElse(null);
    }

    @Override
    public ResponseEntity<User> getUserData() {
        User user = getCurrentUser();
        if (user == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> createUser(
            String name,
            String lastname,
            Role role,
            int vacationDays
    ) throws Exception {
        String token = firebaseAuthFilter.getToken(httpServletRequest);
        FirebaseToken decodedToken = firebaseAuth.verifyIdToken(token);
        User user = new User(decodedToken, name, lastname, vacationDays, role);
        if (userRepository.existsById(user.getUserId()))
            return new ResponseEntity<>("User already exists!", HttpStatus.CONFLICT);
        try {
            userRepository.insert(user);
            return new ResponseEntity<>("Success!", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<String> createVacationRequest(
            LocalDate startDate,
            LocalDate endDate,
            int duration,
            String comment
    ) {
        User user = getCurrentUser();
        if (user == null)
            return new ResponseEntity<>("User could not be found!", HttpStatus.NOT_FOUND);

        if (endDate.isBefore(startDate))
            return new ResponseEntity<>("End date is before start date!", HttpStatus.BAD_REQUEST);

        if (duration < 1)
            return new ResponseEntity<>("Duration must be at least 1 day!", HttpStatus.BAD_REQUEST);

        if (getDaysLeftAndMaxDays(user, startDate.getYear()).getLeftDaysOnlyApproved() < duration)
            return new ResponseEntity<>("Duration exceeds left vacation days!", HttpStatus.BAD_REQUEST);

        if (vacationRequestRepository.isOverlappingWithAnotherVacationRequest(user.getUserId(), startDate, endDate)) {
            return new ResponseEntity<>("Vacation request overlaps with another vacation!", HttpStatus.BAD_REQUEST);
        }


        VacationRequest vacationRequest = new VacationRequest(
                UUID.randomUUID(),
                user,
                startDate,
                endDate,
                duration,
                comment,
                Status.REQUESTED,
                null);
        try {
            vacationRequestRepository.insert(vacationRequest);
            return new ResponseEntity<>("Success!", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<VacationRequest>> getVacationRequestsFromUser() {
        User user = getCurrentUser();
        if (user == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        List<VacationRequest> vacationRequests = vacationRequestRepository.findByUserOrderByVacationStartDesc(user);
        return new ResponseEntity<>(vacationRequests, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<LeftAndMaxVacationDays> getLeftVacationDays(int year) {
        User user = getCurrentUser();
        if (user == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        try {
            LeftAndMaxVacationDays leftAndMaxVacationDays = getDaysLeftAndMaxDays(user, year);
            return new ResponseEntity<>(leftAndMaxVacationDays, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<AllUsersVRResponseBody>> getAllVRs() {
        User currentUser = getCurrentUser();

        if (currentUser == null || currentUser.getRole() != Role.MANAGER) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        List<AllUsersVRResponseBody> responseBody = new ArrayList<>();
        Sort sort = Sort.by(Sort.Direction.DESC, "user").and(Sort.by(Sort.Direction.DESC, "vacationStart"));
        List<VacationRequest> allRequests = vacationRequestRepository.findAll(sort);
        allRequests.stream()
                .collect(Collectors.groupingBy(VacationRequest::getUser))
                .forEach((user, requests) -> {
                    LeftAndMaxVacationDays vacDays = getDaysLeftAndMaxDays(user, Year.now().getValue());
                    responseBody.add(new AllUsersVRResponseBody(user,  vacDays, requests));
                });

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> putVacationRequest(
            String vacationId,
            LocalDate begin,
            LocalDate end,
            Integer days,
            String note,
            Status status,
            String rejection_cause
    ) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        UUID id = UUID.fromString(vacationId);
        Optional<VacationRequest> vacationRequest = vacationRequestRepository.findById(id);
        if (vacationRequest.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        VacationRequest vRequest = vacationRequest.get();
        if (begin != null || end != null) {
            if (begin == null) begin = vRequest.getVacationStart();
            if (end == null) end = vRequest.getVacationEnd();
            if (end.isBefore(begin))
                return new ResponseEntity<>("End date is before start date!", HttpStatus.BAD_REQUEST);
            if (vacationRequestRepository.isOverlappingWithAnotherNotCurrent(currentUser.getUserId(), begin, end, id)) {
                return new ResponseEntity<>("Vacation request overlaps with another vacation!", HttpStatus.BAD_REQUEST);
            }
            vRequest.setVacationStart(begin);
            vRequest.setVacationEnd(end);
        }
        if (days != null) {
            if(begin == null){
                begin = vRequest.getVacationStart();
            }
            if (getDaysLeftAndMaxDays(currentUser, begin.getYear()).getLeftDaysOnlyApproved() < days)
                return new ResponseEntity<>("Duration exceeds left vacation days!", HttpStatus.BAD_REQUEST);

            vRequest.setDuration(days);
        }
        if (note != null) {
            vRequest.setComment(note);
        }
        if (currentUser.getRole() == Role.MANAGER) {
            if (status != null) {
                vRequest.setStatus(status);
            }
            if (rejection_cause != null) {
                vRequest.setRejectReason(rejection_cause);
            }
        } else if (status != null || rejection_cause != null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        vacationRequestRepository.save(vRequest);
        return new ResponseEntity<>("Erfolgreich geändert", HttpStatus.OK);
    }

    private LeftAndMaxVacationDays getDaysLeftAndMaxDays(User user, int year) {
        int maxDays = user.getVacationDays();
        LocalDate lastDayOfYearBefore = LocalDate.of(year - 1, Month.DECEMBER, 31);
        LocalDate firstDayOfNextYear = LocalDate.of(year + 1, Month.JANUARY, 1);
        List<VacationRequest> vacationRequests = vacationRequestRepository.findByUserAndVacationStartAfterAndVacationEndBeforeAndStatusNot(
                user, lastDayOfYearBefore, firstDayOfNextYear, Status.REJECTED
        );
        int vacationDaysWithoutRequested = vacationRequests.stream()
                .filter(request -> request.getStatus() != Status.REQUESTED)
                .mapToInt(VacationRequest::getDuration)
                .sum();

        int vacationDaysWithRequested = vacationRequests.stream()
                .mapToInt(VacationRequest::getDuration)
                .sum();
        return new LeftAndMaxVacationDays(maxDays, maxDays - vacationDaysWithRequested, maxDays - vacationDaysWithoutRequested);
    }

    @Override
    public ResponseEntity<String> deleteVacationRequest(String vacationRequestId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        UUID requestId = UUID.fromString(vacationRequestId);
        Optional<VacationRequest> optionalVacationRequest = vacationRequestRepository.findById(requestId);
        if (optionalVacationRequest.isEmpty()) {
            return new ResponseEntity<>("Vacation Request not found!", HttpStatus.NOT_FOUND);
        }

        VacationRequest vacationRequest = optionalVacationRequest.get();

        if (!vacationRequest.getUser().getUserId().equals(currentUser.getUserId())) {
            return new ResponseEntity<>("Unauthorized: You can only delete your own Vacation Requests!", HttpStatus.UNAUTHORIZED);
        }

        if (vacationRequest.getStatus() == Status.APPROVED) {
            return new ResponseEntity<>("Cannot delete an approved Vacation Request!", HttpStatus.BAD_REQUEST);
        }

        try {
            vacationRequestRepository.deleteByVacationRequestId(requestId);
            return new ResponseEntity<>("Vacation Request deleted successfully!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<VirtualEnvironmentRequest>> getVirtualEnvironmentRequestsFromUser() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        List<VirtualEnvironmentRequest> virtualEnvironmentRequests = virtualEnvironmentRequestRepository.findAllByUser(currentUser);
        return new ResponseEntity<>(virtualEnvironmentRequests, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<VirtualEnvironment>> getVirtualEnvironmentsFromUser() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        List<VirtualEnvironment> virtualEnvironments = virtualEnvironmentRepository.findAllByUser(currentUser);
        return new ResponseEntity<>(virtualEnvironments, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<VirtualEnvironment> setEnvironmentStatus(String id, Status status, String rejectReason) {
        User currentUser = getCurrentUser();
        if (currentUser == null || currentUser.getRole() != Role.MANAGER) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        VirtualEnvironmentRequest vRequest = virtualEnvironmentRequestRepository.findById(UUID.fromString(id)).orElse(null);
        if (vRequest == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        if (vRequest.getStatus().ordinal() != 0 || status == Status.REQUESTED) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        if (status == Status.APPROVED) {
            //send Request to api
            ProvisioningResponse response = provisioningRepository.getTechnicalProvisioning(UUID.fromString(id), vRequest.getEnvironmentType());
            if (response.getVerificationSuccessful()) {
                vRequest.setStatus(Status.APPROVED);
                virtualEnvironmentRequestRepository.save(vRequest);
                VirtualEnvironment virtEnv = new VirtualEnvironment(UUID.randomUUID(),
                        vRequest.getUser(),
                        vRequest.getEnvironmentType(),
                        response.getIpAddress(),
                        response.getUserName(),
                        response.getInitialPassword());
                virtualEnvironmentRepository.save(virtEnv);
                return new ResponseEntity<>(virtEnv, HttpStatus.OK);
            } else {
                vRequest.setStatus(Status.REJECTED);
                vRequest.setRejectReason("Your Request was approved by a Manager but rejected by Provisioning");
                virtualEnvironmentRequestRepository.save(vRequest);
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
        } else {
            vRequest.setStatus(status);
            if (status == Status.REJECTED)
                vRequest.setRejectReason(rejectReason);
            virtualEnvironmentRequestRepository.save(vRequest);
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<String> deleteVirtualEnvironmentRequest(String id) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        UUID requestId = UUID.fromString(id);
        Optional<VirtualEnvironmentRequest> optionalVirtualEnvironmentRequest = virtualEnvironmentRequestRepository.findById(requestId);
        if (optionalVirtualEnvironmentRequest.isEmpty()) {
            return new ResponseEntity<>("Virtual Environment Request not found!", HttpStatus.NOT_FOUND);
        }

        VirtualEnvironmentRequest virtualEnvironmentRequest = optionalVirtualEnvironmentRequest.get();

        if (!virtualEnvironmentRequest.getUser().getUserId().equals(currentUser.getUserId())) {
            return new ResponseEntity<>("Unauthorized: You can only delete your own Virtual Environment Requests!", HttpStatus.UNAUTHORIZED);
        }

        if (virtualEnvironmentRequest.getStatus() == Status.APPROVED) {
            return new ResponseEntity<>("Cannot delete an approved Virtual Environment Request!", HttpStatus.BAD_REQUEST);
        }

        try {
            virtualEnvironmentRequestRepository.deleteByVirtualEnvironmentRequestId(requestId);
            return new ResponseEntity<>("Virtual Environment Request deleted successfully!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public ResponseEntity<String> setVirtualEnvironmentRequestProperties(String id, String environmentType, String comment) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        VirtualEnvironmentRequest vRequest = virtualEnvironmentRequestRepository.findById(UUID.fromString(id)).orElse(null);
        if (vRequest == null) {
            return new ResponseEntity<>("Virtual Environment Request not found!", HttpStatus.NOT_FOUND);
        }
        if (vRequest.getStatus() != Status.REQUESTED) {
            return new ResponseEntity<>("Unauthorized: You can only edit Virtual Environment Requests with status 'Requested'!", HttpStatus.FORBIDDEN);
        }
        if (environmentType != null) vRequest.setEnvironmentType(environmentType);
        if (comment != null) vRequest.setComment(comment);
        virtualEnvironmentRequestRepository.save(vRequest);
        return new ResponseEntity<>("Virtual Environment Request updated successfully!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<AllUsersVEnvRequestResponseBody>> getAllVirtualEnvironmentRequests() {
        User currentUser = getCurrentUser();

        if (currentUser == null || currentUser.getRole() != Role.MANAGER) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        List<AllUsersVEnvRequestResponseBody> responseBody = new ArrayList<>();
        List<VirtualEnvironmentRequest> allRequests = virtualEnvironmentRequestRepository.findAll();
        allRequests.stream()
                .collect(Collectors.groupingBy(VirtualEnvironmentRequest::getUser))
                .forEach((user, virtualEnvironmentRequests) -> responseBody.add(new AllUsersVEnvRequestResponseBody(user, virtualEnvironmentRequests)));
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> createVirtualEnvironmentRequest(
            String environmentType,
            String comment
    ) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        VirtualEnvironmentRequest virtualEnvironmentRequest = new VirtualEnvironmentRequest(
                UUID.randomUUID(),
                currentUser,
                environmentType,
                comment,
                Status.REQUESTED,
                null
        );
        try {
            virtualEnvironmentRequestRepository.insert(virtualEnvironmentRequest);
            return new ResponseEntity<>("Virtual Environment Request created successfully!", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
package com.dhbw.unternehmenb.ssp.controller;

import com.dhbw.unternehmenb.ssp.auth.FirebaseAuthFilter;
import com.dhbw.unternehmenb.ssp.interfaces.ServerApi;
import com.dhbw.unternehmenb.ssp.model.*;
import com.dhbw.unternehmenb.ssp.model.response.AllUsersVRResponseBody;
import com.dhbw.unternehmenb.ssp.view.UserRepository;
import com.dhbw.unternehmenb.ssp.view.VacationRequestRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
    private FirebaseAuth firebaseAuth;
    @Autowired
    private FirebaseAuthFilter firebaseAuthFilter;
    @Autowired
    private HttpServletRequest httpServletRequest;

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

        //TODO: use User Story #31 to check if requested vacation exceeds the limit

        if (vacationRequestRepository.isOverlappingWithAnotherVacationRequest(user.getUserId(), startDate, endDate)){
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
                .forEach((user, requests) -> responseBody.add(new AllUsersVRResponseBody(user, requests)));

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

            List<VacationRequest> allVacations = vacationRequestRepository.findAllByUser(vRequest.getUser());
            LocalDate finalBegin = begin;
            LocalDate finalEnd = end;
            Optional<VacationRequest> filteredVacations = allVacations.stream()
                    .filter(vacationRequest1 ->
                            !vacationRequest1.getVacationRequestId().equals(id) &&
                                    ((!vacationRequest1.getVacationStart().isBefore(finalBegin) &&
                                            !vacationRequest1.getVacationStart().isAfter(finalEnd)) ||
                                            (!vacationRequest1.getVacationEnd().isBefore(finalBegin) &&
                                                    !vacationRequest1.getVacationEnd().isAfter(finalEnd)))
                    )
                    .findFirst();
            if (filteredVacations.isPresent()) {
                return new ResponseEntity<>("Vacation request overlaps with another vacation!", HttpStatus.BAD_REQUEST);
            }
            vRequest.setVacationStart(begin);
            vRequest.setVacationEnd(end);
        }
        if (days != null) {
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
        List<VacationRequest> vacationRequests = vacationRequestRepository.findByUserAndVacationStartAfterAndVacationEndBefore(user, lastDayOfYearBefore, firstDayOfNextYear);
        int leftDays = maxDays;
        int vacationDays = vacationRequests.stream()
                .mapToInt(VacationRequest::getDuration)
                .sum();
        leftDays -= vacationDays;
        return new LeftAndMaxVacationDays(maxDays, leftDays);
    }
  
    @Override
    public ResponseEntity<String> deleteVacationRequest(String vacationRequestId) {
        User currentUser = getCurrentUser();
        if (currentUser == null){
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

}
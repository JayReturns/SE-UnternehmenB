package com.dhbw.unternehmenb.ssp.controller;

import com.dhbw.unternehmenb.ssp.auth.FirebaseAuthFilter;
import com.dhbw.unternehmenb.ssp.interfaces.ServerApi;
import com.dhbw.unternehmenb.ssp.model.Role;
import com.dhbw.unternehmenb.ssp.model.Status;
import com.dhbw.unternehmenb.ssp.model.User;
import com.dhbw.unternehmenb.ssp.model.VacationRequest;
import com.dhbw.unternehmenb.ssp.view.UserRepository;
import com.dhbw.unternehmenb.ssp.view.VacationRequestRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.time.Duration;
import java.util.UUID;

@RestController
public class MainServerController implements ServerApi {
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
            String comment
    ) {
        User user = getCurrentUser();
        if (user == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        if (startDate.isBefore(LocalDate.now()))
            return new ResponseEntity<>("Start date is in the past!", HttpStatus.BAD_REQUEST);

        if (endDate.isBefore(startDate))
            return new ResponseEntity<>("End date is before start date!", HttpStatus.BAD_REQUEST);

        //calculate vacation days
        int vacationDays = (int) Duration
                .between(startDate.atStartOfDay(), endDate.atStartOfDay())
                .toDays()
                + 1;
        //do not count weekends towards vacation days
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            if (date.getDayOfWeek().getValue() > 5)
                vacationDays--;
        }
        //TODO: use User Story #31 to check if requested vacation exceeds the limit

        if (vacationRequestRepository.existsByUserAndVacationStartBetweenOrVacationEndBetween(user, startDate, endDate, startDate, endDate))
            return new ResponseEntity<>("Vacation request overlaps with another vacation!", HttpStatus.BAD_REQUEST);
        if (vacationRequestRepository.existsByUserAndVacationStartIsOrVacationEndIs(user, startDate, endDate))
            return new ResponseEntity<>("Vacation request for those dates already exists!", HttpStatus.BAD_REQUEST);


        VacationRequest vacationRequest = new VacationRequest(
                UUID.randomUUID().toString(),
                user,
                startDate,
                endDate,
                vacationDays,
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
}

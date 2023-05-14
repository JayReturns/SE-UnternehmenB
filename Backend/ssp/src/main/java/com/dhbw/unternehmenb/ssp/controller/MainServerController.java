package com.dhbw.unternehmenb.ssp.controller;

import com.dhbw.unternehmenb.ssp.auth.FirebaseAuthFilter;
import com.dhbw.unternehmenb.ssp.interfaces.ServerApi;
import com.dhbw.unternehmenb.ssp.model.*;
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

import java.util.List;

@RestController
public class MainServerController implements ServerApi {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VacationRequestRepository vacationRequestRepository;
    @Autowired
    private FirebaseAuth firebaseAuth;
    @Autowired
    FirebaseAuthFilter firebaseAuthFilter;
    @Autowired
    HttpServletRequest httpServletRequest;

    private User getCurrentUser() {
        String token = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findById(token).orElse(null);
    }

    @Override
    public ResponseEntity<User> getUserData() {
        User user = getCurrentUser();
        if (user == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        else
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
        try {
            userRepository.save(user);
            return new ResponseEntity<>("Success!", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<VacationRequest>> getVacationRequestsFromUser(OrderParameter orderParameter, OrderDirection orderDirection) throws Exception {
        String token = firebaseAuthFilter.getToken(httpServletRequest);
        FirebaseToken decodedToken = firebaseAuth.verifyIdToken(token);
        User user = userRepository.findById(decodedToken.getUid()).orElse(null);
        if (user == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        else {
            if (orderParameter !=null && orderParameter.equals(OrderParameter.STATUS)) {
                List<VacationRequest> vacationRequests;
                if (orderDirection != null && orderDirection.equals(OrderDirection.ASC)) {
                    vacationRequests = vacationRequestRepository.findByUserOrderByStatusAsc(user);
                } else {
                    vacationRequests = vacationRequestRepository.findByUserOrderByStatusDesc(user);
                }
                return new ResponseEntity<>(vacationRequests, HttpStatus.OK);
            } else {
                List<VacationRequest> vacationRequests;
                if (orderDirection != null && orderDirection.equals(OrderDirection.ASC)) {
                    vacationRequests = vacationRequestRepository.findByUserOrderByVacationStartAsc(user);
                } else {
                    vacationRequests = vacationRequestRepository.findByUserOrderByVacationStartDesc(user);
                }
                return new ResponseEntity<>(vacationRequests, HttpStatus.OK);
            }
        }
    }


}

package com.dhbw.unternehmenb.ssp.controller;

import com.dhbw.unternehmenb.ssp.auth.FirebaseAuthFilter;
import com.dhbw.unternehmenb.ssp.interfaces.ServerApi;
import com.dhbw.unternehmenb.ssp.model.Role;
import com.dhbw.unternehmenb.ssp.model.User;
import com.dhbw.unternehmenb.ssp.model.VacationRequest;
import com.dhbw.unternehmenb.ssp.model.dto.VacationRequestDTO;
import com.dhbw.unternehmenb.ssp.model.response.AllUsersVRResponseBody;
import com.dhbw.unternehmenb.ssp.view.UserRepository;
import com.dhbw.unternehmenb.ssp.view.VacationRequestRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MainServerController implements ServerApi {
    private final Logger logger = LoggerFactory.getLogger(MainServerController.class);
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
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<AllUsersVRResponseBody>> getAllVRs() throws Exception {
        User currentUser = getCurrentUser();

        if (currentUser == null || currentUser.getRole() != Role.MANAGER){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        List<AllUsersVRResponseBody> responseBody = new ArrayList<>();
        
        List<User> users = userRepository.findAll();
        for (User user :
                users) {
            List<VacationRequest> requests = vacationRequestRepository.findByUser(user);
            responseBody.add(new AllUsersVRResponseBody(user, convertRequests(requests) ));
        }
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    private List<VacationRequestDTO> convertRequests ( List<VacationRequest> requests){
        return requests.stream().map(VacationRequest::toDTO).toList();
    }
}

package com.dhbw.unternehmenb.ssp.services;

import com.dhbw.unternehmenb.ssp.Permissions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final FirebaseAuth firebaseAuth;

    public void setUserClaim(String uid, List<Permissions> requestedPermissions) throws FirebaseAuthException{
        List<String> permissions = requestedPermissions
                .stream()
                .map(Enum::toString)
                .toList();

        Map<String, Object> claims = Map.of("custom_claims",permissions);
        firebaseAuth.setCustomUserClaims(uid,claims);
    }

}

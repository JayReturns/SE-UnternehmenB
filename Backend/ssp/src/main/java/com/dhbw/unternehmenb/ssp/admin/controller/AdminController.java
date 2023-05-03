package com.dhbw.unternehmenb.ssp.admin.controller;

import com.dhbw.unternehmenb.ssp.Permissions;
import com.dhbw.unternehmenb.ssp.services.UserManagementService;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserManagementService userManagementService;

    @Secured("ROLE_ANONYMOUS")
    @PostMapping(path = "/user-claim/{uid}")
    public void setUserClaim(
            @PathVariable String uid,
            @RequestBody List<Permissions> requestedClaims

    ) throws FirebaseAuthException {
    userManagementService.setUserClaim(uid,requestedClaims);
    }
}

package com.dhbw.unternehmenb.ssp.model.response;

import com.dhbw.unternehmenb.ssp.model.User;
import com.dhbw.unternehmenb.ssp.model.VirtualEnvironmentRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AllUsersVEnvRequestResponseBody {
    private User user;

    private List<VirtualEnvironmentRequest> requests;
}

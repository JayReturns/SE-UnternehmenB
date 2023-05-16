package com.dhbw.unternehmenb.ssp.model.response;

import com.dhbw.unternehmenb.ssp.model.User;
import com.dhbw.unternehmenb.ssp.model.dto.VacationRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AllUsersVRResponseBody {

    private User user;

    private List<VacationRequestDTO> requests;

}

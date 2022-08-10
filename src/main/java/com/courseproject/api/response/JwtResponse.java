package com.courseproject.api.response;

import com.courseproject.api.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {

    private String token;

    private UserDTO user;

}

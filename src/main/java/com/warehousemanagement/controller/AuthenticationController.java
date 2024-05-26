package com.warehousemanagement.controller;

import com.warehousemanagement.configuration.jwt.JwtTokenUtil;
import com.warehousemanagement.pojo.ApplicationUserDetails;
import com.warehousemanagement.request.AuthenticationRequestDTO;
import com.warehousemanagement.response.AuthenticationResponseDTO;
import com.warehousemanagement.service.UserServiceimpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@Transactional
@RequestMapping("/rest/security")
public class AuthenticationController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserServiceimpl userService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(final HttpServletResponse response, @RequestBody final AuthenticationRequestDTO authenticationRequestDTO) {

        // generate tokens from UserDetails object created by the
        // UserDetailsService
        final ApplicationUserDetails userDetails = userService.getAuthenticationUserDetails(authenticationRequestDTO.getUsername(),
                authenticationRequestDTO.getPassword());

        if (userDetails != null) {
            userService.updateLastLoginDate(authenticationRequestDTO.getUsername());

            //generate access token and add them to response headers
            jwtTokenUtil.generateTokens(response, userDetails.getUsername());
        }

        // Return the user details through response body
        final AuthenticationResponseDTO authResponse = new AuthenticationResponseDTO(userDetails);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}

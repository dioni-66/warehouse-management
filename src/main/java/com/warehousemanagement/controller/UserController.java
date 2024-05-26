package com.warehousemanagement.controller;

import com.warehousemanagement.request.*;
import com.warehousemanagement.response.CreateUserResponseDTO;
import com.warehousemanagement.response.LoadUserResponseDTO;
import com.warehousemanagement.response.UpdateUserResponseDTO;
import com.warehousemanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Transactional
@RequestMapping("/rest/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<CreateUserResponseDTO> createUser(@RequestBody CreateUserRequestDTO createUserRequestDTO){
        CreateUserResponseDTO createUserResponseDTO = userService.createUser(createUserRequestDTO);
        return new ResponseEntity<>(createUserResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/delete")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> deleteUser(@RequestBody DeleteUserRequestDTO deleteUserRequestDTO){
        String message = userService.deleteUser(deleteUserRequestDTO);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/load")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<LoadUserResponseDTO> loadUser(@RequestBody LoadUserRequestDTO loadUserRequestDTO){
        LoadUserResponseDTO loadUserResponseDTO = userService.loadUser(loadUserRequestDTO);
        return new ResponseEntity<>(loadUserResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/update")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UpdateUserResponseDTO> updateUser(@RequestBody UpdateUserRequestDTO updateUserRequestDTO){
        UpdateUserResponseDTO updateUserResponseDTO = userService.updateUser(updateUserRequestDTO);
        return new ResponseEntity<>(updateUserResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/change-password")
    @Secured("ROLE_CLIENT")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequestDTO changePasswordRequestDTO, HttpServletRequest httpServletRequest){
        String message = userService.changePassword(changePasswordRequestDTO, httpServletRequest);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}

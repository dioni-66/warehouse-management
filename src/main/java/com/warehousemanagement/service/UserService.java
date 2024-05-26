package com.warehousemanagement.service;

import com.warehousemanagement.entity.UserEntity;
import com.warehousemanagement.pojo.ApplicationUserDetails;
import com.warehousemanagement.request.*;
import com.warehousemanagement.response.CreateUserResponseDTO;
import com.warehousemanagement.response.LoadUserResponseDTO;
import com.warehousemanagement.response.UpdateUserResponseDTO;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    ApplicationUserDetails getAuthenticationUserDetails(final String username, final String password);

    void updateLastLoginDate(String username);

    CreateUserResponseDTO createUser(CreateUserRequestDTO createUserRequestDTO);

    UserEntity getLoggedInUser(final HttpServletRequest request);

    String deleteUser(DeleteUserRequestDTO deleteUserRequestDTO);

    LoadUserResponseDTO loadUser(LoadUserRequestDTO loadUserRequestDTO);

    UpdateUserResponseDTO updateUser(UpdateUserRequestDTO updateUserRequestDTO);

    String changePassword(ChangePasswordRequestDTO changePasswordRequestDTO, HttpServletRequest httpServletRequest);
}

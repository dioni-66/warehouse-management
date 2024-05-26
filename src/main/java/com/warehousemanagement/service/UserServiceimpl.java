package com.warehousemanagement.service;

import com.warehousemanagement.configuration.jwt.JwtTokenUtil;
import com.warehousemanagement.configuration.security.CustomMd5PasswordEncoder;
import com.warehousemanagement.entity.AuthorityEntity;
import com.warehousemanagement.entity.UserEntity;
import com.warehousemanagement.pojo.ApplicationUserDetails;
import com.warehousemanagement.repository.AuthorityRepository;
import com.warehousemanagement.repository.UserRepository;
import com.warehousemanagement.request.*;
import com.warehousemanagement.response.CreateUserResponseDTO;
import com.warehousemanagement.response.LoadUserResponseDTO;
import com.warehousemanagement.response.UpdateUserResponseDTO;
import com.warehousemanagement.util.ConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserServiceimpl implements UserService {

    /**
     * The Spring Boot authentication manager configured for JWT.
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomMd5PasswordEncoder md5PasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${security.jwt.header:Authorization}")
    private String accessTokenHeader;

    /**
     * Get <code>UserDetails</code> for authentication details for a specific
     * user.
     *
     * @param username - The user's username. - {@link String}
     * @param password - The user's password. - {@link String}
     * @return The user's detail. - {@link ApplicationUserDetails}
     */
    public ApplicationUserDetails getAuthenticationUserDetails(final String username, final String password) {
        // Perform the security
        final Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return (ApplicationUserDetails) authentication.getPrincipal();
    }

    public UserEntity getLoggedInUser(final HttpServletRequest request) {
        final String token = request.getHeader(accessTokenHeader);
        final String username = jwtTokenUtil.getUsernameFromToken(token);
        final UserEntity userEntity = userRepository.findByUsername(username);
        return userEntity;
    }

    public void updateLastLoginDate(String username) {
        final UserEntity user = userRepository.findByUsername(username);
        user.setLastLogin(new Date());
    }

    @Override
    public CreateUserResponseDTO createUser(CreateUserRequestDTO createUserRequestDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEnabled(createUserRequestDTO.isEnabled());
        userEntity.setUsername(createUserRequestDTO.getUsername());
        userEntity.setFirstname(createUserRequestDTO.getFirstname());
        userEntity.setEmail(createUserRequestDTO.getEmail());
        userEntity.setLastname(createUserRequestDTO.getLastname());
        //encode password with MD5
        String encodedPassword = md5PasswordEncoder.encode(createUserRequestDTO.getPassword());
        userEntity.setPassword(encodedPassword);

        //set role to new user
        final AuthorityEntity authorityEntity = authorityRepository.findByName(createUserRequestDTO.getRole());
        if (authorityEntity == null) {
            return new CreateUserResponseDTO("Invalid role!");
        }

        userEntity.setAuthorities(List.of(authorityEntity));

        userRepository.save(userEntity);

        return new CreateUserResponseDTO("User created successfully");
    }

    @Override
    public String deleteUser(DeleteUserRequestDTO deleteUserRequestDTO) {
        UserEntity userEntity = userRepository.findById(deleteUserRequestDTO.getUserId()).orElseThrow(() -> new IllegalArgumentException("Invalid user id"));
        userRepository.delete(userEntity);
        return "User deleted successfully";
    }

    @Override
    public LoadUserResponseDTO loadUser(LoadUserRequestDTO loadUserRequestDTO) {
        UserEntity userEntity = userRepository.findById(loadUserRequestDTO.getUserId()).orElseThrow(() -> new IllegalArgumentException("Invalid user id"));
        ApplicationUserDetails applicationUserDetails = ConverterService.createUserDetailsFromEntity(userEntity);
        return new LoadUserResponseDTO(applicationUserDetails);
    }

    @Override
    public UpdateUserResponseDTO updateUser(UpdateUserRequestDTO updateUserRequestDTO) {
        UserEntity userEntity = userRepository.findById(updateUserRequestDTO.getUserId()).orElseThrow(() -> new IllegalArgumentException("Invalid user id"));
        userEntity.setFirstname(updateUserRequestDTO.getFirstname());
        userEntity.setLastname(updateUserRequestDTO.getLastname());
        userEntity.setEmail(updateUserRequestDTO.getEmail());
        userEntity.setEnabled(updateUserRequestDTO.isEnabled());
        userRepository.save(userEntity);

        //return the updated user
        ApplicationUserDetails applicationUserDetails = ConverterService.createUserDetailsFromEntity(userEntity);
        return new UpdateUserResponseDTO(applicationUserDetails);
    }

    @Override
    public String changePassword(ChangePasswordRequestDTO changePasswordRequestDTO, HttpServletRequest httpServletRequest) {
        String loggedInUser = getLoggedInUser(httpServletRequest).getUsername();
        UserEntity userEntity = userRepository.findByUsername(loggedInUser);

        String encodedCurrentPassword = md5PasswordEncoder.encode(changePasswordRequestDTO.getCurrentPassword());

        if (!userEntity.getPassword().equals(encodedCurrentPassword)) {
            return "Provided current password is not correct!";
        } else if (!changePasswordRequestDTO.getNewPassword().equals(changePasswordRequestDTO.getConfirmNewPassword())) {
            return "'New password' does not match 'Confirm new password'. Please verify input data.";
        } else {
            userEntity.setPassword(md5PasswordEncoder.encode(changePasswordRequestDTO.getNewPassword()));
            userRepository.save(userEntity);
            return "Password changed successfully";
        }

    }

}

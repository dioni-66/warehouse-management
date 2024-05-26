package com.warehousemanagement.util;

import com.warehousemanagement.entity.AuthorityEntity;
import com.warehousemanagement.entity.UserEntity;
import com.warehousemanagement.pojo.ApplicationUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConverterService {
    public static ApplicationUserDetails createUserDetailsFromEntity(final UserEntity user) {
        final ApplicationUserDetails details = new ApplicationUserDetails();
        details.setId(user.getId());
        details.setUsername(user.getUsername());
        details.setEmail(user.getEmail());
        details.setEnabled(user.getEnabled());
        details.setPassword(user.getPassword());
        details.setLastLogin(user.getLastLogin());
        details.setAuthorities(mapToGrantedAuthorities(user.getAuthorities()));
        details.setFirstName(user.getFirstname());
        details.setLastName(user.getLastname());
        return details;
    }

    /**
     * Extracts the roles of the user.
     *
     * @param authorities a list of the <code>Authority</code> objects of the user.
     * @return A list of <code>GrantedAuthority</code> objects.
     */
    public static List<GrantedAuthority> mapToGrantedAuthorities(List<AuthorityEntity> authorities) {
        return authorities.stream().map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());
    }

}

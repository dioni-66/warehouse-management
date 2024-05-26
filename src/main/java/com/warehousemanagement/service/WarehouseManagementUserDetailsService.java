package com.warehousemanagement.service;

import com.warehousemanagement.entity.UserEntity;
import com.warehousemanagement.repository.UserRepository;
import com.warehousemanagement.util.ConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WarehouseManagementUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConverterService converterService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Wrong login name or password");
        } else {
            return converterService.createUserDetailsFromEntity(user);
        }
    }



}

package com.example.Demohs.util;

import com.example.Demohs.Dto.UserAuthDataDto;
import com.example.Demohs.Entity.UserAuthData;
import com.example.Demohs.Service.UserAuthDataService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserAuthDataService userAuthDataService;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws IllegalArgumentException {
        UserAuthData userAuthData = userAuthDataService.findByUserName(username);

        // Assuming userRoles is a List<String> in UserAuthDataDto
        List<String> userRoles = userAuthData.getUserRole();

        Set<SimpleGrantedAuthority> authorities = Optional.ofNullable(userRoles)
                .map(roles -> roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());

        String password = userAuthData.getPassword();
        return new org.springframework.security.core.userdetails.User(userAuthData.getUsername(), password, authorities);
    }

    private String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

}

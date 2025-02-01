package com.bravos2k5.bravosshop.service;

import com.bravos2k5.bravosshop.model.user.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        if(username.matches("^([\\w-.]+){1,64}@(\\w&&[^_]+){2,255}.[a-z]{2,}$")) {
            user = userService.findByEmail(username);
        } else {
            user = userService.findByUsername(username);
        }
        if(user == null) throw new UsernameNotFoundException("User not found");
        return user;
    }

}

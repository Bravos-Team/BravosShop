package com.bravos2k5.bravosshop.config.security;

import com.bravos2k5.bravosshop.model.user.User;
import com.bravos2k5.bravosshop.service.UserService;
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
        String emailRegex = "^[\\p{Alnum}_+&*-]+(?:\\.[\\p{Alnum}_+&*-]+)*@(?:[\\p{Alnum}-]+\\.)+\\p{Alpha}{2,7}$";
        if(username.matches(emailRegex)) {
            user = userService.findByEmail(username);
        } else {
            user = userService.findByUsername(username);
        }
        if(user == null) throw new UsernameNotFoundException("User not found");
        return user;
    }

}

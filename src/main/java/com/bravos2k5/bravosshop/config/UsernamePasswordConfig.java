package com.bravos2k5.bravosshop.config;

import com.bravos2k5.bravosshop.config.security.CustomUserDetailsService;
import com.bravos2k5.bravosshop.service.constract.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class UsernamePasswordConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return new CustomUserDetailsService(userService);
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        provider.setPreAuthenticationChecks(toCheck -> {
            if(toCheck.getPassword() == null || toCheck.getPassword().isBlank()) {
                throw new BadCredentialsException("User account doesn't have password");
            }
            if (!toCheck.isEnabled()) {
                throw new DisabledException("User account is not enabled");
            }
            if(!toCheck.isAccountNonLocked()) {
                throw new LockedException("User account is banned");
            }
        });
        return provider::authenticate;
    }



}

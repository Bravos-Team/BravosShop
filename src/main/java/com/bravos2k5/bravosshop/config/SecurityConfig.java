package com.bravos2k5.bravosshop.config;

import com.bravos2k5.bravosshop.config.filter.CustomAuthenticationFilter;
import com.bravos2k5.bravosshop.config.filter.JwtFilter;
import com.bravos2k5.bravosshop.config.handler.CustomFailureHandler;
import com.bravos2k5.bravosshop.config.handler.CustomSuccessHandler;
import com.bravos2k5.bravosshop.service.AuthService;
import com.bravos2k5.bravosshop.service.CustomOAuth2UserService;
import com.bravos2k5.bravosshop.service.JwtService;
import com.bravos2k5.bravosshop.utils.IdentifyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final CustomFailureHandler customFailureHandler;
    private final AuthService authService;
    private final JwtService jwtService;

    @Autowired
    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomSuccessHandler customSuccessHandler,
                          CustomFailureHandler customFailureHandler, AuthService authService, JwtService jwtService) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customSuccessHandler = customSuccessHandler;
        this.customFailureHandler = customFailureHandler;
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(authService,jwtService);
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter(AuthenticationManager authenticationManager) {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager);
        customAuthenticationFilter.setPostOnly(true);
        customAuthenticationFilter.setFilterProcessesUrl("/p/login");
        customAuthenticationFilter.setAuthenticationSuccessHandler(customSuccessHandler);
        customAuthenticationFilter.setAuthenticationFailureHandler(customFailureHandler);
        return customAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain security(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

        http.csrf(CsrfConfigurer::disable);
        http.sessionManagement(session -> {
           session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        });

        http.oauth2Login(oauth2Login -> {
            oauth2Login.loginPage("/login");
            oauth2Login.userInfoEndpoint(userInfo -> userInfo.userService(this.customOAuth2UserService));
            oauth2Login.successHandler(customSuccessHandler);
            oauth2Login.failureHandler(customFailureHandler);
        });

        http.formLogin(form -> {
            form.loginPage("/login");
            form.loginProcessingUrl("/p/login");
            form.permitAll();
        });

        http.logout(logout -> {
            logout.logoutUrl("/logout");
            logout.clearAuthentication(true);
            logout.deleteCookies("token");
            logout.logoutSuccessUrl("/home");
            logout.permitAll();
        });

        http.authorizeHttpRequests(request -> {
            request.requestMatchers("/actuator").permitAll();
            request.requestMatchers("/res/**").permitAll();
            request.requestMatchers("/p/**", "/","/login","/error").permitAll();
            request.requestMatchers("/r/**").authenticated();
            request.requestMatchers("/a/**").hasRole("ADMIN");
            request.anyRequest().denyAll();
        });

        http.addFilterAt(customAuthenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtFilter(), CustomAuthenticationFilter.class);

        return http.build();
    }

}

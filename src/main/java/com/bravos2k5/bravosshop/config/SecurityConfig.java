package com.bravos2k5.bravosshop.config;

import com.bravos2k5.bravosshop.service.CustomOAuth2UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    @Autowired
    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomSuccessHandler customSuccessHandler) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customSuccessHandler = customSuccessHandler;
    }

    @Bean
    public SecurityFilterChain security(HttpSecurity http, JwtFilter jwtFilter) throws Exception {

        http.csrf(CsrfConfigurer::disable);
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.oauth2Login(oauth2Login -> {
            oauth2Login.loginPage("/login");
            oauth2Login.userInfoEndpoint(userInfo -> userInfo.userService(this.customOAuth2UserService));
            oauth2Login.successHandler(customSuccessHandler);
        });

        http.formLogin(form -> {
            form.loginPage("/login");
            form.successHandler(customSuccessHandler);
            form.failureUrl("/login?error=true");
        });

        http.logout(logout -> {
            logout.logoutUrl("/logout");
            logout.permitAll();
            logout.clearAuthentication(true);
            logout.deleteCookies("token");
            logout.logoutSuccessUrl("/home");
        });

        http.authorizeHttpRequests(request -> {
            request.requestMatchers("/res/**").permitAll();
            request.requestMatchers("/p/**", "/","/login","/error").permitAll();
            request.requestMatchers("/r/**").authenticated();
            request.requestMatchers("/a/**").hasRole("ADMIN");
            request.anyRequest().denyAll();
        });

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}

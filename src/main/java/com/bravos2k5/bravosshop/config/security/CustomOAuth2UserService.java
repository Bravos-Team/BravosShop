package com.bravos2k5.bravosshop.config.security;

import com.bravos2k5.bravosshop.model.user.User;
import com.bravos2k5.bravosshop.service.UserService;
import com.bravos2k5.bravosshop.utils.IdentifyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger logger = Logger.getLogger(CustomOAuth2UserService.class.getName());

    private final BCryptPasswordEncoder passwordEncoder;
    private final IdentifyGenerator identifyGenerator;
    private final UserService userService;

    @Autowired
    public CustomOAuth2UserService(UserService userService, BCryptPasswordEncoder passwordEncoder,
                                   IdentifyGenerator identifyGenerator) {
        this.passwordEncoder = passwordEncoder;
        this.identifyGenerator = identifyGenerator;
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String googleId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        if(googleId == null || name == null || email == null) {
            logger.warning("Error when get userData");
            return null;
        }
        User user = userService.findByEmail(email);
        if (user == null) {
            long newId = identifyGenerator.generateId();
            user = User
                    .builder()
                    .id(newId)
                    .username(email.substring(0,email.indexOf("@")) + "-" + suffixGenerate(googleId))
                    .displayName(name)
                    .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .email(email)
                    .googleId(googleId)
                    .enabled(true)
                    .build();
            user = userService.createNewUser(user);
        }
        else if (user.getGoogleId() == null){
            user.setGoogleId(googleId);
            user = userService.updateUser(user);
        }
        Map<String,Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("username",user.getUsername());
        attributes.put("displayName",user.getDisplayName());
        return new DefaultOAuth2User(user.getAuthorities(),attributes,"sub");
    }

    private String suffixGenerate(String googleId) {
        return (Integer.parseInt(googleId.substring(0,5)) + System.currentTimeMillis() % 10) + "";
    }

}
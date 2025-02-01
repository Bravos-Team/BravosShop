package com.bravos2k5.bravosshop.service.impl;

import com.bravos2k5.bravosshop.model.cart.Cart;
import com.bravos2k5.bravosshop.model.user.User;
import com.bravos2k5.bravosshop.repo.CartRepository;
import com.bravos2k5.bravosshop.repo.UserRepository;
import com.bravos2k5.bravosshop.service.UserService;
import com.bravos2k5.bravosshop.utils.IdentifyGenerator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final IdentifyGenerator identifyGenerator;
    private final CartRepository cartRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, IdentifyGenerator identifyGenerator,
                           CartRepository cartRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.identifyGenerator = identifyGenerator;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createNewUser(User user, HttpServletRequest request) {
        if (userRepository.existsByEmailOrUsername(user.getEmail(),user.getUsername())) {
            return null;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String cartId = null;
        for(Cookie cookie : request.getCookies()) {
            if(cookie.getName().equals("cartId")) {
                cartId = cookie.getValue();
            }
        }
        if(cartId == null) {
            user.setCart(new Cart(identifyGenerator.generateId(1), user));
        }
        else {
            Cart cart = cartRepository.findById(Long.valueOf(cartId)).orElse(null);
            if (cart == null || cart.getUser() != null) {
                cart = new Cart(identifyGenerator.generateId(1),user);
            }
            else {
                cart.setUser(user);
            }
            user.setCart(cart);
        }
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User updateUserIfExist(User user) {
        User userExist = userRepository.findById(user.getId()).orElse(null);
        if(userExist == null) return null;
        if(userExist.getUsername() != null) userExist.setUsername(user.getUsername());
        if(userExist.getEmail() != null) userExist.setEmail(user.getEmail());
        if(userExist.getPhone() != null) userExist.setPhone(user.getPhone());
        if(userExist.getDefaultAddress() != null) userExist.setDefaultAddress(user.getDefaultAddress());
        if(userExist.getAddresses() != null) userExist.setAddresses(user.getAddresses());
        if(userExist.getGoogleId() != null) userExist.setGoogleId(user.getGoogleId());
        if(userExist.getEnabled() != null) userExist.setEnabled(user.getEnabled());
        if(userExist.getStatus() != null) userExist.setStatus(user.getStatus());
        return userRepository.saveAndFlush(userExist);
    }


}

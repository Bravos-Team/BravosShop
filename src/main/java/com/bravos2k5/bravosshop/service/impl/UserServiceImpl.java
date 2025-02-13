package com.bravos2k5.bravosshop.service.impl;

import com.bravos2k5.bravosshop.dto.user.UserAdminDto;
import com.bravos2k5.bravosshop.model.cart.Cart;
import com.bravos2k5.bravosshop.model.user.User;
import com.bravos2k5.bravosshop.repo.CartRepository;
import com.bravos2k5.bravosshop.repo.UserRepository;
import com.bravos2k5.bravosshop.service.CookieService;
import com.bravos2k5.bravosshop.service.UserService;
import com.bravos2k5.bravosshop.utils.IdentifyGenerator;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final IdentifyGenerator identifyGenerator;
    private final CartRepository cartRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CookieService cookieService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, IdentifyGenerator identifyGenerator,
                           CartRepository cartRepository, BCryptPasswordEncoder passwordEncoder, CookieService cookieService) {
        this.userRepository = userRepository;
        this.identifyGenerator = identifyGenerator;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
        this.cookieService = cookieService;
    }

    @Override
    public User createNewUser(User user) {
        if (userRepository.existsByEmailOrUsername(user.getEmail(), user.getUsername())) {
            return null;
        }
        Cookie cartCookie = cookieService.getCookie("cart");
        String cartId = null;
        if (cartCookie != null) {
            cartId = cartCookie.getValue();
        }
        if (cartId == null || cartId.isBlank()) {
            user.setCart(new Cart(identifyGenerator.generateId(1), user));
        } else {
            Cart cart = cartRepository.findById(Long.valueOf(cartId)).orElse(null);
            if (cart == null || cart.getUser() != null) {
                cart = new Cart(identifyGenerator.generateId(1), user);
            } else {
                cart.setUser(user);
            }
            user.setCart(cart);
        }
        if (user.getId() == null) {
            user.setId(identifyGenerator.generateId(1));
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
        if (userExist == null) return null;
        if (user.getUsername() != null) userExist.setUsername(user.getUsername());
        if (user.getEmail() != null) userExist.setEmail(user.getEmail());
        if (user.getDisplayName() != null) userExist.setDisplayName(user.getDisplayName());
        if (user.getBirthDate() != null) userExist.setBirthDate(user.getBirthDate());
        if (user.getPhone() != null) userExist.setPhone(user.getPhone());
        if (user.getDefaultAddress() != null) userExist.setDefaultAddress(user.getDefaultAddress());
        if (user.getAddresses() != null) userExist.setAddresses(user.getAddresses());
        if (user.getGoogleId() != null) userExist.setGoogleId(user.getGoogleId());
        if (user.getEnabled() != null) userExist.setEnabled(user.getEnabled());
        if (user.getStatus() != null) userExist.setStatus(user.getStatus());
        return userRepository.saveAndFlush(userExist);
    }

    @Override
    public boolean existByUsernameOrEmail(String username, String email) {
        return userRepository.existsByEmailOrUsername(email, username);
    }

    @Override
    public Page<UserAdminDto> getAllAdminUserDto(int pageNumber,int pageSize) {
        return userRepository.getAllUserAdminDto(PageRequest.of( (pageNumber - 1),pageSize));
    }


}

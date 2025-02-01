package com.bravos2k5.bravosshop.service;

import com.bravos2k5.bravosshop.model.user.User;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    User createNewUser(User user, HttpServletRequest request);

    User findById(Long id);

    User findByUsername(String username);

    User findByEmail(String email);

    User updateUser(User user);

    User updateUserIfExist(User user);

}

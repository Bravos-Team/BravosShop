package com.bravos2k5.bravosshop.service;

import com.bravos2k5.bravosshop.model.user.User;

public interface UserService {

    User createNewUser(User user);

    User findById(Long id);

    User findByUsername(String username);

    User findByEmail(String email);

    User updateUser(User user);

    User updateUserIfExist(User user);

    boolean existByUsernameOrEmail(String username, String email);

}

package com.bravos2k5.bravosshop.service;

import com.bravos2k5.bravosshop.dto.user.UserAdminDto;
import com.bravos2k5.bravosshop.model.user.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    User createNewUser(User user);

    User findById(Long id);

    User findByUsername(String username);

    User findByEmail(String email);

    User updateUser(User user);

    User updateUserIfExist(User user);

    User getProfile();

    boolean existByUsernameOrEmail(String username, String email);

    Page<UserAdminDto> getAllAdminUserDto(int pageNumber,int pageSize);
}

package com.bravos2k5.bravosshop.service.interfaces;

import com.bravos2k5.bravosshop.dto.UserAdminDto;
import com.bravos2k5.bravosshop.model.user.User;
import org.springframework.data.domain.Page;

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

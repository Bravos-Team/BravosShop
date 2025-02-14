package com.bravos2k5.bravosshop.repo;

import com.bravos2k5.bravosshop.dto.user.UserAdminDto;
import com.bravos2k5.bravosshop.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByGoogleId(String googleId);


    @Query("select  new com.bravos2k5.bravosshop.dto.user.UserAdminDto(u.id, u.username,u.displayName, u.email, u.enabled)" + " from User u")
    Page<UserAdminDto> getAllUserAdminDto(Pageable pageable);
}

package com.bravos2k5.bravosshop.user.repository;

import com.bravos2k5.bravosshop.user.dto.user.UserAdminDto;
import com.bravos2k5.bravosshop.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    @Query("select  new com.bravos2k5.bravosshop.dto.user.UserAdminDto(u.id, u.username,u.displayName, u.email, u.enabled)" + " from User u")
    Page<UserAdminDto> getAllUserAdminDto(Pageable pageable);

    @Query("select count(u) > 0 from User u where u.email = :email or u.username = :username")
    boolean existsByEmailOrUsername(@Param("email") String email, @Param("username") String username);

}

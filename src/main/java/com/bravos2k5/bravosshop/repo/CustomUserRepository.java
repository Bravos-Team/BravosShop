package com.bravos2k5.bravosshop.repo;

public interface CustomUserRepository {

    boolean existsByEmailOrUsername(String email, String username);

}

package com.bravos2k5.bravosshop.repo.impl;

import com.bravos2k5.bravosshop.repo.CustomUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

public class CustomUserRepositoryImpl implements CustomUserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public boolean existsByEmailOrUsername(String email, String username) {
        if(email != null && username != null) {
            return entityManager.createQuery("SELECT COUNT(u) > 0 FROM User u" +
                            " WHERE u.email = :email OR u.username = :username", Boolean.class)
                    .setParameter("email", email)
                    .setParameter("username", username)
                    .getSingleResult();
        }
        return false;
    }

}

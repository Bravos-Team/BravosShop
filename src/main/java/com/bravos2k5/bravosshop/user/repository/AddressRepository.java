package com.bravos2k5.bravosshop.user.repository;

import com.bravos2k5.bravosshop.user.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {


    @Query("select a from Address  a where a.user.id = :userId")
    List<Address> findAllByUserId(Long userId);
}

package com.example.restaurantsoftware.repository;

import com.example.restaurantsoftware.model.KitchenBarStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KitchenBarStaffRepository extends JpaRepository<KitchenBarStaff, Long> {

    Optional<KitchenBarStaff> findByPassword(String password);

    boolean existsByPassword(String password);

}

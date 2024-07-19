package com.example.restaurantsoftware.repository;

import com.example.restaurantsoftware.model.Waiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WaiterRepository extends JpaRepository<Waiter, Long> {
    Optional<Waiter> findWaiterByPassword(String password);

    boolean existsWaiterByPassword(String password);
}

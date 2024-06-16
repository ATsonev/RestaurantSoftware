package com.example.restaurantsoftware.repository;

import com.example.restaurantsoftware.model.Bill;
import com.example.restaurantsoftware.model.enums.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    @Query("FROM bills where dateAndTimeFinished between :startDate AND :endDate")
    List<Bill> findByDateAndTimeFinishedBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT b FROM bills b WHERE b.dateAndTimeFinished BETWEEN :startDate AND :endDate AND b.waiter.id = :waiterId")
    List<Bill> findByDateAndTimeFinishedBetweenAndWaiter(LocalDateTime startDate, LocalDateTime endDate, Long waiterId);

    @Query("SELECT b FROM bills b WHERE b.dateAndTimeFinished BETWEEN :startDate AND :endDate AND b.paymentMethod = :paymentMethod")
    List<Bill> findByDateAndTimeFinishedBetweenAndPaymentMethod(LocalDateTime startDate, LocalDateTime endDate, PaymentMethod paymentMethod);
    @Query("SELECT b FROM bills b WHERE b.dateAndTimeFinished BETWEEN :startDate AND :endDate" +
            " AND b.paymentMethod = :paymentMethod AND b.waiter.id = :waiterId")
    List<Bill> findByDateAndTimeFinishedBetweenAAndWaiterAndPaymentMethod(LocalDateTime startDate, LocalDateTime endDate , Long waiterId, PaymentMethod paymentMethod);

}

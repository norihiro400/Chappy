package com.example.book_manager_app.repository;

import com.example.book_manager_app.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByBookIdOrderByReservationDateAsc(Long bookId);
    boolean existsByBookIdAndUserId(Long bookId, Long userId);
}

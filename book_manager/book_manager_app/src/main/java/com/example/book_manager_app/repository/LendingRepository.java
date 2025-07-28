package com.example.book_manager_app.repository;

import com.example.book_manager_app.domain.Lending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LendingRepository extends JpaRepository<Lending, Long> {
    List<Lending> findByReturnDateIsNull(); // 返却されていない貸出
    List<Lending> findByReturnDueDateBeforeAndReturnDateIsNull(LocalDate date);
    Lending findByBookIdAndReturnDateIsNull(Long bookId);
}

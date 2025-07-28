package com.example.book_manager_app.service;

import com.example.book_manager_app.domain.Book;
import com.example.book_manager_app.domain.Lending;
import com.example.book_manager_app.domain.Reservation;
import com.example.book_manager_app.domain.User;
import com.example.book_manager_app.repository.BookRepository;
import com.example.book_manager_app.repository.LendingRepository;
import com.example.book_manager_app.repository.ReservationRepository;
import com.example.book_manager_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LendingRepository lendingRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }

    public List<Book> searchBooks(String keyword) {
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrIsbnContainingIgnoreCase(keyword, keyword, keyword);
    }

    @Transactional
    public Lending lendBook(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (!book.isAvailable()) {
            throw new RuntimeException("Book is not available for lending");
        }

        Lending lending = new Lending();
        lending.setBook(book);
        lending.setUser(user);
        lending.setLendingDate(LocalDate.now());
        lending.setReturnDueDate(LocalDate.now().plusWeeks(2)); // 貸出期間は2週間

        book.setAvailable(false);
        bookRepository.save(book);

        return lendingRepository.save(lending);
    }

    @Transactional
    public Lending returnBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
        Lending lending = lendingRepository.findByBookIdAndReturnDateIsNull(bookId);

        if (lending == null) {
            throw new RuntimeException("Book is not currently lent out");
        }

        lending.setReturnDate(LocalDate.now());
        lendingRepository.save(lending);

        book.setAvailable(true);
        bookRepository.save(book);

        // 予約がある場合、次の予約者に通知するロジックをここに追加することも可能
        List<Reservation> reservations = reservationRepository.findByBookIdOrderByReservationDateAsc(bookId);
        if (!reservations.isEmpty()) {
            // TODO: 次の予約者に通知する処理
            // 例: メール送信サービスを呼び出す
        }

        return lending;
    }

    @Transactional
    public Reservation reserveBook(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (reservationRepository.existsByBookIdAndUserId(bookId, userId)) {
            throw new RuntimeException("You have already reserved this book.");
        }

        // 貸出中の場合のみ予約可能
        if (book.isAvailable()) {
            throw new RuntimeException("Book is currently available, no need to reserve.");
        }

        Reservation reservation = new Reservation();
        reservation.setBook(book);
        reservation.setUser(user);
        reservation.setReservationDate(LocalDate.now());

        return reservationRepository.save(reservation);
    }

    public List<Lending> findOverdueLendings() {
        return lendingRepository.findByReturnDueDateBeforeAndReturnDateIsNull(LocalDate.now());
    }

    public List<Lending> findCurrentLendings() {
        return lendingRepository.findByReturnDateIsNull();
    }

    public List<Reservation> findReservationsByBookId(Long bookId) {
        return reservationRepository.findByBookIdOrderByReservationDateAsc(bookId);
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }
}

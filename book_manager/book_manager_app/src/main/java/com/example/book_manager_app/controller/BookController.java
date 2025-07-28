package com.example.book_manager_app.controller;

import com.example.book_manager_app.domain.Book;
import com.example.book_manager_app.domain.Lending;
import com.example.book_manager_app.domain.Reservation;
import com.example.book_manager_app.domain.User;
import com.example.book_manager_app.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.findAllBooks());
        return "books/list";
    }

    @GetMapping("/search")
    public String searchBooks(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Book> books;
        if (keyword != null && !keyword.isEmpty()) {
            books = bookService.searchBooks(keyword);
        } else {
            books = bookService.findAllBooks();
        }
        model.addAttribute("books", books);
        model.addAttribute("keyword", keyword);
        return "books/list";
    }

    @GetMapping("/{id}")
    public String bookDetail(@PathVariable Long id, Model model) {
        bookService.findBookById(id).ifPresent(book -> {
            model.addAttribute("book", book);
            model.addAttribute("currentLending", bookService.findCurrentLendings().stream()
                    .filter(l -> l.getBook().getId().equals(id) && l.getReturnDate() == null)
                    .findFirst().orElse(null));
            model.addAttribute("reservations", bookService.findReservationsByBookId(id));
        });
        return "books/detail";
    }

    @PostMapping("/{bookId}/lend")
    public String lendBook(@PathVariable Long bookId, @RequestParam Long userId, RedirectAttributes redirectAttributes) {
        try {
            bookService.lendBook(bookId, userId);
            redirectAttributes.addFlashAttribute("message", "書籍を貸し出しました。");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/books/" + bookId;
    }

    @PostMapping("/{bookId}/return")
    public String returnBook(@PathVariable Long bookId, RedirectAttributes redirectAttributes) {
        try {
            bookService.returnBook(bookId);
            redirectAttributes.addFlashAttribute("message", "書籍を返却しました。");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/books/" + bookId;
    }

    @PostMapping("/{bookId}/reserve")
    public String reserveBook(@PathVariable Long bookId, @RequestParam Long userId, RedirectAttributes redirectAttributes) {
        try {
            bookService.reserveBook(bookId, userId);
            redirectAttributes.addFlashAttribute("message", "書籍を予約しました。");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/books/" + bookId;
    }

    // ユーザー登録・選択用の仮実装
    @GetMapping("/users")
    public String listUsers(Model model) {
        // 仮のユーザーリスト。実際には認証機能などが必要
        model.addAttribute("users", bookService.saveUser(new User())); // ダミーユーザー作成
        return "users/list";
    }

    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "books/add";
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute Book book, RedirectAttributes redirectAttributes) {
        book.setAvailable(true);
        bookService.saveBook(book);
        redirectAttributes.addFlashAttribute("message", "書籍を追加しました。");
        return "redirect:/books";
    }
}

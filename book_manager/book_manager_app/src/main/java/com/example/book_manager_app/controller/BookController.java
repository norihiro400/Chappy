package com.example.book_manager_app.controller;

import com.example.book_manager_app.domain.Book;
import com.example.book_manager_app.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
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
    public String lendBook(@PathVariable Long bookId, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            bookService.lendBook(bookId, principal.getName());
            redirectAttributes.addFlashAttribute("message", "書籍を貸し出しました。");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/books/" + bookId;
    }

    @PostMapping("/{bookId}/return")
    public String returnBook(@PathVariable Long bookId, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            bookService.returnBook(bookId, principal.getName());
            redirectAttributes.addFlashAttribute("message", "書籍を返却しました。");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/books/" + bookId;
    }

    @PostMapping("/{bookId}/reserve")
    public String reserveBook(@PathVariable Long bookId, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            bookService.reserveBook(bookId, principal.getName());
            redirectAttributes.addFlashAttribute("message", "書籍を予約しました。");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/books/" + bookId;
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('TEACHER')")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "books/add";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('TEACHER')")
    public String addBook(@ModelAttribute Book book, RedirectAttributes redirectAttributes) {
        book.setAvailable(true);
        bookService.saveBook(book);
        redirectAttributes.addFlashAttribute("message", "書籍を追加しました。");
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public String showEditBookForm(@PathVariable Long id, Model model) {
        bookService.findBookById(id).ifPresent(book -> model.addAttribute("book", book));
        return "books/edit";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public String editBook(@PathVariable Long id, @ModelAttribute Book book, RedirectAttributes redirectAttributes) {
        book.setId(id);
        bookService.saveBook(book);
        redirectAttributes.addFlashAttribute("message", "書籍情報を更新しました。");
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookService.deleteBookById(id);
        redirectAttributes.addFlashAttribute("message", "書籍を削除しました。");
        return "redirect:/books";
    }
}

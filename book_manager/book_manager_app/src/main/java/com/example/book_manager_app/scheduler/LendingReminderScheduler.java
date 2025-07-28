package com.example.book_manager_app.scheduler;

import com.example.book_manager_app.domain.Lending;
import com.example.book_manager_app.service.BookService;
import com.example.book_manager_app.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class LendingReminderScheduler {

    @Autowired
    private BookService bookService;

    @Autowired
    private MailService mailService;

    // 毎日午前9時に実行
    @Scheduled(cron = "0 0 9 * * ?")
    public void sendLendingReminders() {
        List<Lending> overdueLendings = bookService.findOverdueLendings();

        for (Lending lending : overdueLendings) {
            String to = lending.getUser().getEmail();
            String subject = "【書籍貸出管理】返却期限のお知らせ";
            String text = String.format(
                    "%s様\n\n貸出中の書籍「%s」の返却期限が過ぎています。\n返却期限日: %s\n\n速やかにご返却をお願いいたします。\n\n書籍貸出管理システム",
                    lending.getUser().getUsername(),
                    lending.getBook().getTitle(),
                    lending.getReturnDueDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
            );
            mailService.sendSimpleMessage(to, subject, text);
        }

        // 返却期限が近い書籍への通知（例：3日後が期限の書籍）
        List<Lending> currentLendings = bookService.findCurrentLendings();
        for (Lending lending : currentLendings) {
            if (lending.getReturnDueDate().isEqual(LocalDate.now().plusDays(3))) {
                String to = lending.getUser().getEmail();
                String subject = "【書籍貸出管理】返却期限が近づいています";
                String text = String.format(
                        "%s様\n\n貸出中の書籍「%s」の返却期限が近づいています。\n返却期限日: %s\n\nご返却の準備をお願いいたします。\n\n書籍貸出管理システム",
                        lending.getUser().getUsername(),
                        lending.getBook().getTitle(),
                        lending.getReturnDueDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                );
                mailService.sendSimpleMessage(to, subject, text);
            }
        }
    }
}

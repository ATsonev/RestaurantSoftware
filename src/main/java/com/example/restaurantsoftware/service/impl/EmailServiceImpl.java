package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.Waiter;
import com.example.restaurantsoftware.repository.WaiterRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailServiceImpl {

    private final JavaMailSender mailSender;
    private final WaiterRepository waiterRepository;
    private final BillServiceImpl billService;

    public EmailServiceImpl(JavaMailSender mailSender, WaiterRepository waiterRepository, BillServiceImpl billService) {
        this.mailSender = mailSender;
        this.waiterRepository = waiterRepository;
        this.billService = billService;
    }

    @Scheduled(cron = "0 01 * * * ?")
    public void sendTurnoverEmail() {
        String toEmail = "alexconev1@gmail.com";
        String subject = "Daily Turnover Report - " + LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        StringBuilder text = new StringBuilder();
        text.append("<html>");
        text.append("<body>");
        text.append("<h1>Daily Turnover Report</h1>");
        text.append("<p><strong>The total turnover for the previous day is: </strong>");
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now();
        text.append(billService.getTurnover(from, to, "all", "all"));
        text.append("</p>");
        text.append("<h2>Turnover by Waiter</h2>");
        text.append("<ul>");
        for(Waiter waiter : waiterRepository.findAll()) {
            text.append("<li><strong>")
                    .append(waiter.getFirstName()).append(" ").append(waiter.getLastName())
                    .append(":</strong> ")
                    .append(billService.getTurnover(from, to, String.valueOf(waiter.getId()), "all"))
                    .append("</li>");
        }
        text.append("</ul>");
        text.append("<h2>Payment Method Breakdown</h2>");
        text.append("<p><strong>Payed with card:</strong> ")
                .append(billService.getTurnover(from, to, "all", "Card"))
                .append("</p>");
        text.append("<p><strong>Payed with cash:</strong> ")
                .append(billService.getTurnover(from, to, "all", "Cash"))
                .append("</p>");
        text.append("</body>");
        text.append("</html>");

        sendEmail(toEmail, subject, text.toString());
    }

    public void sendEmail(String to, String subject, String text) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}

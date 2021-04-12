package toeicLab.toeicLab.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import toeicLab.toeicLab.domain.MailDto;
import toeicLab.toeicLab.domain.Member;
import toeicLab.toeicLab.repository.MailRepository;

@Service
@AllArgsConstructor
public class MailService {
    private JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "YOUR_EMAIL_ADDRESS";


    public MailDto mailSend(MailDto mailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getEmail());
        message.setFrom(MailService.FROM_ADDRESS);
        message.setSubject("[ToeicLab]회원님의 이메일 인증번호입니다.");
        message.setText(mailDto.getEmailCheckToken());

        System.out.println("emailCheckToken = " + mailDto.getEmailCheckToken());
        mailSender.send(message);
        return mailDto;
    }

    public void resetPasswordMailSend(MailDto mailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getEmail());
        message.setFrom(MailService.FROM_ADDRESS);
        message.setSubject("[ToeicLab]회원님의 비밀번호 초기화 인증번호입니다.");
        message.setText(mailDto.getEmailCheckToken());

        System.out.println("emailCheckToken = " + mailDto.getEmailCheckToken());
        mailSender.send(message);
        return;
    }

}

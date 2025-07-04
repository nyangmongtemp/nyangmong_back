package com.playdata.userservice.user.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailSenderService {

    private final JavaMailSender mailSender;

    /**
     *
     * @param email
     * @return
     * @throws MessagingException
     */
    // 회원가입 인증코드를 이메일로 전송하는 로직
    public String joinMain (String email) throws MessagingException {
        String setFrom = "secun77@gmail.com";
        String id = String.valueOf(((int)(Math.random() * 9000) + 1000));
        String toMail = email;
        String title = " 냥몽 회원가입 인증 이메일";
        String content = "냥몽 홈페이지 가입을 신청해 주셔서 감사합니다." +
                "<br><br>" +
                "인증 번호는 <strong>" + id+ "</strong> 입니다. <br>" +
                "해당 인증 번호를 인증번호 확인란에 기입해 주세요.";

        mailSend(setFrom, toMail, title, content);
        
        // 인증코드를 리턴
        return id;
    }

    /**
     *
     * @param email
     * @return
     * @throws MessagingException
     */
    // 개인정보 변경 시 인증코드를 이메일로 전송하는 로직
    public String sendAuthCode(String email) throws MessagingException {
        String setFrom = "secun77@gmail.com";
        String id = String.valueOf(((int)(Math.random() * 9000) + 1000));
        String toMail = email;
        String title = " 냥몽 개인정보 변경 인증 이메일";
        String content = """
        <html>
            <body>
                <h3>회원정보 변경을 위한 인증 코드입니다.</h3>
                <p>아래의 인증 코드를 입력해 주세요:</p>
                <div style="font-size: 24px; font-weight: bold; color: #2E86C1; margin-top: 10px;">
                    %s
                </div>
                <p style="margin-top: 20px; color: #555;">※ 인증 코드는 5분 동안만 유효합니다.</p>
            </body>
        </html>
    """.formatted(id);

        mailSend(setFrom, toMail, title, content);
        // 인증코드를 리턴
        return id;
    }

    /**
     *
     * @param setFrom
     * @param toMail
     * @param title
     * @param content
     * @throws MessagingException
     */
    // 실제로 이메일을 전송해주는 로직
    public void mailSend(String setFrom, String toMail, String title, String content) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
        helper.setTo(setFrom);
        helper.setTo(toMail);
        helper.setSubject(title);
        helper.setText(content, true);
        mailSender.send(mimeMessage);

    }

}
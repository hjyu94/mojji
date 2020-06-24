package me.hjeong.mojji;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.config.AppProperties;
import me.hjeong.mojji.mail.EmailMessage;
import me.hjeong.mojji.mail.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.activation.FileDataSource;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class TestController {

    private final EmailService emailService;
    private final AppProperties appProperties;
    private final TemplateEngine templateEngine;

    @GetMapping("/send")
    public String send() {
        Context context = new Context();
        context.setVariable("nickname", "닉네임");
        context.setVariable("message", "로그인 하려면 아래 링크를 클릭하세요.");
        context.setVariable("host", appProperties.getHost());
        context.setVariable("link", "/login-by-email?");
        context.setVariable("linkName", "스터디올래 로그인하기");
        String message = templateEngine.process("email", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to("hjeong.you@gmail.com")
                .subject("[모찌마켓] 회원 가입 인증")
                .message(message)
                .build();
        emailService.sendEmail(emailMessage);
        return "index";
    }
}

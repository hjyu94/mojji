package me.hjeong.mojji.account;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.domain.Account;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    public void createNewAccount(SignUpForm signUpForm) {
        String password = passwordEncoder.encode(signUpForm.getPassword());
        signUpForm.setPassword(password);
        Account account = modelMapper.map(signUpForm, Account.class);
        Account newAccount = accountRepository.save(account);
        this.sendSignupEmail(newAccount);
    }

    private void sendSignupEmail(Account account) {
        account.generateEmailCheckToken();
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(account.getEmail());
        mailMessage.setSubject("스터디올래, 회원 가입 인증");
        mailMessage.setText(
                "/check-email-token?"
                        + "token=" + account.getEmailCheckToken()
                        + "&email=" + account.getEmail()
        );
        javaMailSender.send(mailMessage);
    }
}

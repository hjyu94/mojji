package me.hjeong.mojji.account;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.domain.Account;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    public void createNewAccount(RegisterForm registerForm) {
        String password = passwordEncoder.encode(registerForm.getPassword());
        registerForm.setPassword(password);
        Account account = modelMapper.map(registerForm, Account.class);
        Account newAccount = accountRepository.save(account);
        this.sendRegisterEmail(newAccount);
    }

    private void sendRegisterEmail(Account account) {
        account.generateEmailCheckToken();
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(account.getEmail());
        mailMessage.setSubject("스터디올래, 회원 가입 인증");
        mailMessage.setText(
                "/confirmed-account-by-email?"
                        + "token=" + account.getEmailCheckToken()
                        + "&email=" + account.getEmail()
        );
        javaMailSender.send(mailMessage);
    }

    public boolean validateRegisterEmailToken(String token, Account account) {
        return account != null && token.equals(account.getEmailCheckToken());
    }

}

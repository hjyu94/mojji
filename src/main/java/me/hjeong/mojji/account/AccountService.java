package me.hjeong.mojji.account;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.domain.Account;
import me.hjeong.mojji.mail.EmailMessage;
import me.hjeong.mojji.mail.EmailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    AuthenticationManager authenticationManager;

    public Account createNewAccount(RegisterForm registerForm) {
        String password = passwordEncoder.encode(registerForm.getPassword());
        registerForm.setPassword(password);
        Account account = modelMapper.map(registerForm, Account.class);
        Account newAccount = accountRepository.save(account);
        this.sendRegisterEmail(newAccount);

        return newAccount;
    }

    public void sendRegisterEmail(Account account) {
        account.setSendEmailAt(LocalDateTime.now());
        account.generateEmailCheckToken();
        EmailMessage emailMessage = EmailMessage.builder()
                .to(account.getEmail())
                .subject("[Mojji] 회원 가입 인증")
                .message("/confirmed-account-by-email?"
                        + "token=" + account.getEmailCheckToken()
                        + "&email=" + account.getEmail())
                .build();
        emailService.sendEmail(emailMessage);
    }

    public boolean validateRegisterEmailToken(String token, Account account) {
        return account != null && token.equals(account.getEmailCheckToken());
    }

    public void login(Account account) {
        // TODO:: 정석적인 방법으로 수정할 것! 스프링 시큐리티 공부하기!
        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(
                new UserAccount(account),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
    }

    @Override
    @Transactional(readOnly = true)
    public UserAccount loadUserByUsername(String nicknameOrEmail) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(nicknameOrEmail);
        if(null == account) {
            account = accountRepository.findByNickname(nicknameOrEmail);
        }
        if(null == account) {
            throw new UsernameNotFoundException(nicknameOrEmail);
        }

        return new UserAccount(account);
    }

    public boolean canResendRegisterEmail(Account account) {
        return account.getSendEmailAt().isBefore(LocalDateTime.now().minusHours(1));
    }

    public void
    sendResetPasswordEmail(Account account) {
        String temppassword = UUID.randomUUID().toString();
        account.setPassword(passwordEncoder.encode(temppassword));
        EmailMessage emailMessage = EmailMessage.builder()
                .to(account.getEmail())
                .subject("[Mojji] 회원 가입 인증")
                .message(temppassword)
                .build();
        emailService.sendEmail(emailMessage);
    }

    public void confirmRegisterEmail(Account account) {
        account.setEmailVerified(true);
        account.setJoinedAt(LocalDateTime.now());
        account.setEmailCheckToken(null);
    }

}

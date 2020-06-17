package me.hjeong.mojji.account;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.domain.Account;
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

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

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
}
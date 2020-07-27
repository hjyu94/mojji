package me.hjeong.mojji.module.account.service;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.infra.config.AppProperties;
import me.hjeong.mojji.infra.mail.EmailMessage;
import me.hjeong.mojji.infra.mail.EmailService;
import me.hjeong.mojji.module.account.Account;
import me.hjeong.mojji.module.account.UserAccount;
import me.hjeong.mojji.module.account.event.AccountCreatedEvent;
import me.hjeong.mojji.module.account.form.RegisterForm;
import me.hjeong.mojji.module.account.repository.AccountRepository;
import me.hjeong.mojji.module.category.Category;
import me.hjeong.mojji.module.chat.service.ChatService;
import me.hjeong.mojji.module.notification.NotificationService;
import me.hjeong.mojji.module.post.PostService;
import me.hjeong.mojji.module.setting.form.PasswordForm;
import me.hjeong.mojji.module.setting.form.ProfileForm;
import me.hjeong.mojji.module.station.Station;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

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
    private final AppProperties appProperties;
    private final TemplateEngine templateEngine;
    private final PostService postService;
    private final NotificationService notificationService;
    private final ChatService chatService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public Account createNewAccount(RegisterForm registerForm) {
        String password = passwordEncoder.encode(registerForm.getPassword());
        registerForm.setPassword(password);
        Account account = modelMapper.map(registerForm, Account.class);
        Account newAccount = accountRepository.save(account);
        this.sendRegisterEmail(newAccount);
        applicationEventPublisher.publishEvent(new AccountCreatedEvent(newAccount));

        return newAccount;
    }

    public void sendRegisterEmail(Account account) {
        account.setSendEmailAt(LocalDateTime.now());
        account.generateEmailCheckToken();

        Context context = new Context();
        context.setVariable("nickname", account.getNickname());
        context.setVariable("message", "회원가입을 완료하려면 아래 링크를 클릭해주세요.");
        context.setVariable("host", appProperties.getHost());
        context.setVariable("link", "/confirmed-account-by-email?"
                + "token=" + account.getEmailCheckToken()
                + "&email=" + account.getEmail());
        context.setVariable("linkName", "모찌 가입 완료");
        String message = templateEngine.process("email", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(account.getEmail())
                .subject("[모찌마켓] 회원 가입 인증")
                .message(message)
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

    public void sendResetPasswordEmail(Account account) {
        String temp_password = UUID.randomUUID().toString();
        account.setPassword(passwordEncoder.encode(temp_password));

        Context context = new Context();
        context.setVariable("nickname", account.getNickname());
        context.setVariable("message", "패쓰워드를 변경하였습니다. 다음 비밀번호로 로그인 후 패쓰워드를 꼭 변경해주세요 : " + temp_password);
        context.setVariable("host", appProperties.getHost());
        context.setVariable("link", "/login");
        context.setVariable("linkName", "로그인하기");
        String message = templateEngine.process("email", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(account.getEmail())
                .subject("[모찌마켓] 패스워드 갱신")
                .message(message)
                .build();
        emailService.sendEmail(emailMessage);
    }

    public void confirmRegisterEmail(Account account) {
        account.setEmailVerified(true);
        account.setJoinedAt(LocalDateTime.now());
        account.setEmailCheckToken(null);
        this.login(account); // update current login user
    }

    public void updateProfile(ProfileForm form, Account account) {
        account.setNickname(form.getNickname());
        if(!form.getProfileImage().isBlank()) {
            account.setProfileImage(form.getProfileImage());
        }
        account.setNotiByEmail(form.isNotiByEmail());
        account.setNotiByWeb(form.isNotiByWeb());
        accountRepository.save(account);
        login(account);
    }

    public void updatePassword(PasswordForm passwordForm, Account account) {
        String encode = passwordEncoder.encode(passwordForm.getNewPassword());
        account.setPassword(encode);
        accountRepository.save(account);
        login(account);
    }

    public void delete(Account account) {
        postService.deleteAllByWriter(account);
        notificationService.deleteAllByAccount(account);
        chatService.deleteAllIncludingAccount(account);

        account.setCategories(null);
        account.setStations(null);
        accountRepository.delete(account);
        accountRepository.flush();
    }

    public void addStation(Station station, Account account) {
        account.getStations().add(station);
    }

    public void removeStation(Station station, Account account) {
        account.getStations().remove(station);
    }

    public void addCategory(Category category, Account account) {
        account.getCategories().add(category);
    }

    public void removeCategory(Category category, Account account) {
        account.getCategories().remove(category);
    }
}

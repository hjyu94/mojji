package me.hjeong.mojji;

import me.hjeong.mojji.account.AccountRepository;
import me.hjeong.mojji.account.AccountService;
import me.hjeong.mojji.account.RegisterForm;
import me.hjeong.mojji.domain.Account;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
//
//@Component
//public class AppRunner implements ApplicationRunner {
//    @Autowired
//    AccountService accountService;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        RegisterForm registerForm = new RegisterForm();
//        registerForm.setNickname("유효정");
//        registerForm.setPassword("12345678");
//        registerForm.setConfirmPassword("12345678");
//        registerForm.setEmail("hjeong.you@gmail.com");
//
//        Account account = accountService.createNewAccount(registerForm);
//        accountService.login(account);
//    }
//}

package me.hjeong.mojji.account;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.domain.Account;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final ModelMapper modelMapper;
    private final RegisterFormValidator registerFormValidator;

    @InitBinder("registerForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(registerFormValidator);
    }

    @GetMapping("/login")
    public String loginForm() { return "login"; }

    @GetMapping("/new-account")
    public String registerForm(Model model) {
        model.addAttribute(new RegisterForm());
        return "/account/register";
    }

    @PostMapping("/new-account")
    public String registerUser(@Valid RegisterForm registerForm, Errors errors, Model model) throws Exception {
        if(errors.hasErrors()) {
            model.addAttribute(registerForm);
            return "/account/register";
        }
        Account account = accountService.createNewAccount(registerForm);
        accountService.login(account);
        return "redirect:/";
    }

    @GetMapping("/confirmed-account-by-email")
    public String confirmRegisterEmail(@RequestParam String token, @RequestParam String email, Model model) {
        Account account = accountRepository.findByEmail(email);
        model.addAttribute("title", "Verify Email");

        // 토큰이 일치하지 않다면
        if(!accountService.validateRegisterEmailToken(token, account)) {
            model.addAttribute("error", "올바르지 않은 접근입니다");
        }
        else {
            account.confirmRegisterEmail();
            model.addAttribute("messaege", "가입이 완료되었습니다.");
        }
        return "account/message";
    }

    @GetMapping("/account/confirm-email")
    public String checkEmail(@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account") Account account, Model model) {
        if(account.isEmailVerified()) { // 이미 메일 인증을 마친 상태라면 홈으로 이동
            return "redirect:/";
        }
        model.addAttribute("email", account.getEmail());
        return "account/check-email";
    }

}
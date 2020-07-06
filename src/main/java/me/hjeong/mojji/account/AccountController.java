package me.hjeong.mojji.account;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.domain.Account;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.NoSuchElementException;

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
        return "account/register";
    }

    @PostMapping("/new-account")
    public String registerUser(@Valid RegisterForm registerForm, Errors errors, Model model) throws Exception {
        if(errors.hasErrors()) {
            model.addAttribute(registerForm);
            return "account/register";
        }
        Account account = accountService.createNewAccount(registerForm);
        accountService.login(account);
        return "redirect:/";
    }

    @GetMapping("/confirmed-account-by-email")
    public String confirmRegisterEmail(@RequestParam String token
            , @RequestParam String email
            , Model model
            , RedirectAttributes attributes)
    {
        Account account = accountRepository.findByEmail(email);

        String title = "";
        String message = "";

        // 토큰이 일치하지 않다면
        if(!accountService.validateRegisterEmailToken(token, account)) {
            title = "Error";
            message = "올바르지 않은 접근입니다";
        }
        else {
            accountService.confirmRegisterEmail(account);
            title = "Let's Start";
            message = "가입이 완료되었습니다.";
        }

        attributes.addFlashAttribute("title", title);
        attributes.addFlashAttribute("message", message);
        return "redirect:/account/message";
    }

    // 홈에서 메일 인증 확인 메세지를 클릭한 경우
    @GetMapping("/account/confirm-email")
    public String checkConfirmEmail(@CurrentAccount Account account, Model model) {
        if(account.isEmailVerified()) { // 이미 메일 인증을 마친 상태라면 홈으로 이동
            return "redirect:/";
        }
        model.addAttribute("email", account.getEmail());
        return "account/check-email";
    }

    @PostMapping("/account/confirm-email")
    public String resendConfirmEmail(@CurrentAccount Account account, Model model, RedirectAttributes attributes) {
        if(account.isEmailVerified()) { // 이미 메일 인증을 마친 유저라면
            return "redirect:/";
        }

        String message = "";
        if(!accountService.canResendRegisterEmail(account)) {
            message = "한시간 이내에 메세지를 보냈습니다. 받은 메세지함을 다시 확인해 주세요.";
        } else {
            accountService.sendRegisterEmail(account);
            message = "이메일을 다시 전송했습니다";
        }

        attributes.addFlashAttribute("title", "Check Email");
        attributes.addFlashAttribute("message", message);
        return "redirect:/account/message";
    }

    @GetMapping("/account/message")
    public String message()
    {
        return "account/message";
    }

    @GetMapping("/account/password")
    public String forgotPassword(@CurrentAccount Account account) {
        // 로그인한 사용자가 비밀번호를 찾으려고 하는 경우 홈으로 리다이렉트한다.
        if (account != null) {
            return "redirect:/";
        }
        return "account/forgot";
    }

    @PostMapping("/account/password")
    public String sendForgotPassowordEmail(@RequestParam String email, RedirectAttributes attributes) {
        String title = "", message = "";
        Account account = accountRepository.findByEmail(email);
        if(null == account) {
            title = "Error";
            message = "No that email account";
        } else {
            accountService.sendResetPasswordEmail(account);
            title = "Reset Password";
            message = "we send reset password to your email.";
        }
        attributes.addFlashAttribute("title", title);
        attributes.addFlashAttribute("message", message);
        return "redirect:/account/message";
    }

    @GetMapping("/account/profile/{nickname}")
    public String showProfile(@PathVariable String nickname, Model model) {
        Account account = accountRepository.findByNickname(nickname);
        if(account == null) {
            throw new NoSuchElementException("해당하는 유저를 찾을 수 없습니다");
        }
        model.addAttribute(account);
        return "account/profile";
    }

}
package me.hjeong.mojji.account;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final ModelMapper modelMapper;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute(new SignUpForm());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(@Valid SignUpForm signUpForm, Errors errors, Model model) {
        if(errors.hasErrors()) {
            model.addAttribute(signUpForm);
            return "sign-up";
        }
        accountService.createNewAccount(signUpForm);
        return "redirect:/";
    }

}
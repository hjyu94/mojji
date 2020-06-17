package me.hjeong.mojji.main;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.account.CurrentAccount;
import me.hjeong.mojji.account.UserAccount;
import me.hjeong.mojji.domain.Account;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/")
    public String home(@CurrentAccount Account account, Model model) {
        if (account != null) {
            model.addAttribute(account);
        }
        return "index";
    }

}

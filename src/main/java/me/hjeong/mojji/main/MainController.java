package me.hjeong.mojji.main;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.account.UserAccount;
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
    public String home(@AuthenticationPrincipal UserAccount userAccount, Model model) {
        if(userAccount != null) {
            model.addAttribute(userAccount.getAccount());
        }
        return "index";
    }

}

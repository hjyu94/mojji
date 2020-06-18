package me.hjeong.mojji.setting;

import me.hjeong.mojji.account.CurrentAccount;
import me.hjeong.mojji.domain.Account;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SettingController {

    @GetMapping("/setting/profile")
    public String profileForm(@CurrentAccount Account account) {
        return "setting/profile";
    }

    @GetMapping("/setting/password")
    public String passwordForm(@CurrentAccount Account account) {
        return "setting/password";
    }

    @GetMapping("/setting/location")
    public String locationForm(@CurrentAccount Account account) {
        return "setting/location";
    }

    @GetMapping("/setting/exit")
    public String exitForm(@CurrentAccount Account account) {
        return "setting/exit";
    }
}

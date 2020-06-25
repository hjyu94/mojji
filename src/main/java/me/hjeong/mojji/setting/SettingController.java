package me.hjeong.mojji.setting;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.account.AccountRepository;
import me.hjeong.mojji.account.CurrentAccount;
import me.hjeong.mojji.domain.Account;
import me.hjeong.mojji.setting.form.ProfileForm;
import me.hjeong.mojji.setting.validator.ProfileFormValidator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
@RequiredArgsConstructor
public class SettingController {

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final ProfileFormValidator profileFormValidator;

    @InitBinder("profileForm")
    public void initBinderToProfileForm(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(profileFormValidator);
    }

    @GetMapping("/setting/profile")
    public String profileForm(@CurrentAccount Account account, Model model) {
        Account accountWithLocation = accountRepository.findByEmail(account.getEmail());
        // Lazy loading 으로 인해 zone 정보를 가지고 있지 않다.
        ProfileForm profile = modelMapper.map(accountWithLocation, ProfileForm.class);
        model.addAttribute(accountWithLocation);
        model.addAttribute("profile", profile);
        return "setting/profile";
    }

    // Id 중복 체크
    @GetMapping("/setting/profile/idcheck")
    @ResponseBody
    public int idCheck(@RequestParam String nickname) {
        if(accountRepository.existsByNickname(nickname)) {
            return 1;
        } else {
            return 0;
        }
    }

    @PostMapping("/setting/profile")
    public String updateProfile(@CurrentAccount Account account, ProfileForm profileForm, Errors errors, Model model) {
        if(errors.hasErrors()) {
            model.addAttribute(account);
            model.addAttribute(profileForm);
            return "setting/profile";
        }
        ProfileFormValidator profileFormValidator = new ProfileFormValidator();
        profileFormValidator.validate(profileForm, errors);
        if(errors.hasErrors()) {
            model.addAttribute(profileForm);
            return "setting/profile";
        }
        return "redirect:/setting/profile";
    }

    @GetMapping("/setting/password")
    public String passwordForm(@CurrentAccount Account account, Model model) {
        Account byId = accountRepository.findById(account.getId()).get();
        model.addAttribute(byId);
        return "setting/password";
    }


    // 회원 확인
    @ResponseBody
    @RequestMapping(value = "/idCheck", method = RequestMethod.POST)
    public int postIdCheck(HttpServletRequest req, @RequestBody Object obj) throws Exception {
        String userId = req.getParameter("userId");
        Account account = accountRepository.findByNickname(userId);
        if(account == null) { // no user
            return 0;
        } else {
            return 1;
        }
    }

    @GetMapping("/setting/location")
    public String locationForm(@CurrentAccount Account account, Model model) {
        Account byId = accountRepository.findById(account.getId()).get();
        model.addAttribute(byId);
        return "setting/location";
    }

    @GetMapping("/setting/quit")
    public String quitForm(@CurrentAccount Account account, Model model) {
        Account byId = accountRepository.findById(account.getId()).get();
        model.addAttribute(byId);
        return "setting/quit";
    }
}

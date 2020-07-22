package me.hjeong.mojji.module.setting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.module.account.Account;
import me.hjeong.mojji.module.account.CurrentAccount;
import me.hjeong.mojji.module.account.repository.AccountRepository;
import me.hjeong.mojji.module.account.service.AccountService;
import me.hjeong.mojji.module.category.Category;
import me.hjeong.mojji.module.category.CategoryRepository;
import me.hjeong.mojji.module.setting.form.PasswordForm;
import me.hjeong.mojji.module.setting.form.ProfileForm;
import me.hjeong.mojji.module.setting.form.QuitForm;
import me.hjeong.mojji.module.setting.validator.PasswordFormValidator;
import me.hjeong.mojji.module.setting.validator.QuitFormValidator;
import me.hjeong.mojji.module.station.Station;
import me.hjeong.mojji.module.station.StationRepository;
import me.hjeong.mojji.module.station.StationService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class SettingController {

    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final StationRepository stationRepository;
    private final StationService stationService;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper; // 스프링 부트는 fastXml이 제공하는 objectMapper가 기본적으로 의존성이 들어와 있고 빈으로 등록되어 있다. (object<->json)
    private final PasswordFormValidator passwordFormValidator;
    private final QuitFormValidator quitFormValidator;

    @InitBinder("passwordForm")
    public void initBinderForPasswordForm(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(passwordFormValidator);
    }

    @InitBinder("quitForm")
    public void initBinderForQuitForm(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(quitFormValidator);
    }

    @GetMapping("/setting/profile")
    public String profileForm(@CurrentAccount Account account, Model model) {
        Account foundAccount = accountRepository.findAccountWithStationsAndCategoriesById(account.getId());
        ProfileForm profile = modelMapper.map(foundAccount, ProfileForm.class);
        model.addAttribute(foundAccount);
        model.addAttribute(profile);
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
    public String updateProfile(@CurrentAccount Account account, @Valid ProfileForm profileForm, Errors errors, Model model, RedirectAttributes attributes) {
        if(errors.hasErrors()) {
            model.addAttribute(account);
            return "setting/profile";
        }
        accountService.updateProfile(profileForm, account);
        attributes.addFlashAttribute("message", "<strong>수정완료!</strong> 프로필을 수정하였습니다");
        return "redirect:/setting/profile";
    }

    @GetMapping("/setting/password")
    public String passwordForm(@CurrentAccount Account account, Model model) {
        Account foundAccount = accountRepository.findAccountWithStationsAndCategoriesById(account.getId());
        model.addAttribute(foundAccount);
        model.addAttribute(new PasswordForm());
        return "setting/password";
    }

    @PostMapping("/setting/password")
    public String updatePassword(@CurrentAccount Account account, @Valid PasswordForm passwordForm, Errors errors, Model model, RedirectAttributes attributes) {
        if(errors.hasErrors()) {
            Account foundAccount = accountRepository.findAccountWithStationsAndCategoriesById(account.getId());
            model.addAttribute(foundAccount);
            return "setting/password";
        }
        accountService.updatePassword(passwordForm, account);
        attributes.addFlashAttribute("message", "<strong>수정완료!</strong> 비밀번호를 수정하였습니다");
        return "redirect:/setting/password";
    }

    @GetMapping("/setting/category")
    public String targetForm(@CurrentAccount Account account, Model model) throws JsonProcessingException {
        Account foundAccount = accountRepository.findAccountWithStationsAndCategoriesById(account.getId());
        model.addAttribute(foundAccount);

        List<String> collect = foundAccount.getCategories().stream().map(Category::getTitle).collect(Collectors.toList());
        model.addAttribute("categories", collect);

        // 선택목록
        List<String> whitelist = categoryRepository.findAll().stream().map(Category::getTitle).collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(whitelist));

        return "setting/category";
    }

    @PostMapping("/setting/category/add")
    @ResponseBody
    public ResponseEntity addCategory(@CurrentAccount Account account, @RequestParam("title") String title) {
        Account foundAccount = accountRepository.findAccountWithCategoriesById(account.getId());
        Category category = categoryRepository.findByTitle(title);
        accountService.addCategory(category, foundAccount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/setting/category/remove")
    @ResponseBody
    public ResponseEntity removeCategory(@CurrentAccount Account account, @RequestParam("title") String title) {
        Account foundAccount = accountRepository.findAccountWithCategoriesById(account.getId());
        Category category = categoryRepository.findByTitle(title);
        accountService.removeCategory(category, foundAccount);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/setting/station")
    public String locationForm(@CurrentAccount Account account, Model model) throws JsonProcessingException {
        Account foundAccount = accountRepository.findAccountWithStationsAndCategoriesById(account.getId());
        model.addAttribute(foundAccount);

        // 유저 동네 String List로 보내기
        List<String> collect = foundAccount.getStations().stream().map(Station::toString).collect(Collectors.toList());
        model.addAttribute("stations", collect);

        List<String> regions = Arrays.asList("수도권", "부산", "대구", "광주", "대전");
        model.addAttribute("regions", regions);

        return "setting/station";
    }

    @PostMapping("/setting/station/requestRegion")
    @ResponseBody
    public Object getStationLines(@RequestParam String region) {
        List<Station> allByRegion = stationRepository.findAllByRegion(region);
        TreeSet<String> lines = new TreeSet<>();
        for(Station station : allByRegion) {
            lines.add(station.getLine());
        }
        return lines;
    }

    @PostMapping("/setting/station/requestName")
    @ResponseBody
    public Object getStationNames(@RequestParam String region, @RequestParam String line) {
        List<Station> allByRegionAndLine = stationRepository.findAllByRegionAndLine(region, line);
        TreeSet<String> lines = new TreeSet<>();
        for(Station station : allByRegionAndLine) {
            lines.add(station.getName());
        }
        return lines;
    }

    @PostMapping("/setting/station/add")
    @ResponseBody
    public Object addStation(@CurrentAccount Account account, Station station) {
        Optional<Station> optStation = stationRepository.findByRegionAndLineAndName(station.getRegion(), station.getLine(),station.getName());
        if(optStation.isPresent()) {
            Account accountWithStations = accountRepository.findAccountWithStationsById(account.getId());
            accountService.addStation(optStation.get(), accountWithStations);
            List<String> stations = accountWithStations.getStations().stream().map(Station::toString).collect(Collectors.toList());
            return stations;
        }
        return null;
    }

    @PostMapping("/setting/station/remove")
    @ResponseBody
    public Object removeStation(@CurrentAccount Account account, @RequestParam("station") String stationStr) {
        Station station = stationService.getStationByString(stationStr);
        Account accountWithStations = accountRepository.findAccountWithStationsById(account.getId());
        accountService.removeStation(station, accountWithStations);
        List<String> stations = accountWithStations.getStations().stream().map(Station::toString).collect(Collectors.toList());
        return stations;
    }

    @GetMapping("/setting/quit")
    public String quitForm(@CurrentAccount Account account, Model model) {
        Account foundAccount = accountRepository.findAccountWithStationsAndCategoriesById(account.getId());
        model.addAttribute(foundAccount);
        model.addAttribute(new QuitForm());
        return "setting/quit";
    }

    @PostMapping("/setting/quit")
    public String quit(@CurrentAccount Account account, @Valid QuitForm quitForm, Errors errors, Model model, HttpServletRequest request) {
        if(errors.hasErrors()) {
            Account foundAccount = accountRepository.findAccountWithStationsAndCategoriesById(account.getId());
            model.addAttribute(foundAccount);
            return "setting/quit";
        }
        accountService.delete(account);
        request.getSession().invalidate();
        return "account/quit";
    }
}

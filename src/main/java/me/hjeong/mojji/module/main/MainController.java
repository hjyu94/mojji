package me.hjeong.mojji.module.main;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hjeong.mojji.module.account.repository.AccountRepository;
import me.hjeong.mojji.module.account.CurrentAccount;
import me.hjeong.mojji.module.account.Account;
import me.hjeong.mojji.module.post.Post;
import me.hjeong.mojji.infra.formatter.KeywordsFormatter;
import me.hjeong.mojji.module.post.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    private final PostRepository postRepository;
    private final KeywordsFormatter keywordsFormatter;
    private final AccountRepository accountRepository;

    @InitBinder("keywords")
    public void keywordInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addCustomFormatter(keywordsFormatter);
    }

    @GetMapping("/")
    public String home(@CurrentAccount Account account, Model model) {
        if (account != null) {
            account = accountRepository.findAccountWithStationsAndCategoriesById(account.getId());
            List<Post> categoryPosts = postRepository.findFirst9ByCategoriesOrderByCreatedDateTimeDesc(account.getCategories());
            List<Post> stationPosts = postRepository.findFirst9ByStationsOrderByCreatedDateTimeDesc(account.getStations());
            model.addAttribute("stationPosts", stationPosts);
            model.addAttribute("categoryPosts", categoryPosts);
        } else {
            List<Post> posts = postRepository.findFirst9ByOrderByCreatedDateTimeDesc();
            model.addAttribute("posts", posts);
        }
        model.addAttribute("account", account);
        return "index";
    }

    @GetMapping("/search")
    public String search(@CurrentAccount Account account, @RequestParam List<String> keywords, Model model
            , @PageableDefault(size = 9, sort = "createdDateTime", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("search, keywords : {}", keywords);
        if(keywords != null) {
            Page<Post> postPage = postRepository.findByKeywords(pageable, keywords);
            if (!postPage.hasContent()) {
                postPage = null;
            }
            model.addAttribute("account", account);
            model.addAttribute("keywords", keywords);
            model.addAttribute("posts", postPage);
            return "post/search";
        } else {
            return "redirect:/posts";
        }
    }

}

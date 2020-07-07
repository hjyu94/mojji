package me.hjeong.mojji.main;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hjeong.mojji.account.CurrentAccount;
import me.hjeong.mojji.domain.Account;
import me.hjeong.mojji.domain.Post;
import me.hjeong.mojji.formatter.KeywordsFormatter;
import me.hjeong.mojji.post.PostRepository;
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

    @InitBinder("keywords")
    public void keywordInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addCustomFormatter(keywordsFormatter);
    }

    @GetMapping("/")
    public String home(@CurrentAccount Account account, Model model) {
        if (account != null) {
            model.addAttribute(account);
        }
        List<Post> posts = postRepository.findFirst6ByOrderByCreatedDateTimeDesc();
        model.addAttribute("posts", posts);
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

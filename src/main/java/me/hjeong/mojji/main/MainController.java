package me.hjeong.mojji.main;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.account.CurrentAccount;
import me.hjeong.mojji.domain.Account;
import me.hjeong.mojji.domain.Post;
import me.hjeong.mojji.post.PostRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final PostRepository postRepository;

    @GetMapping("/")
    public String home(@CurrentAccount Account account, Model model) {
        if (account != null) {
            model.addAttribute(account);
        }
        List<Post> posts = postRepository.findFirst6ByOrderByCreatedDateTimeDesc();
        model.addAttribute("posts", posts);
        return "index";
    }

}

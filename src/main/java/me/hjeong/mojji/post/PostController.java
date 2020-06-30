package me.hjeong.mojji.post;

import me.hjeong.mojji.account.CurrentAccount;
import me.hjeong.mojji.domain.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PostController {

    @GetMapping("/new-post")
    public String newPost(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute("form", new PostForm());
        return "post/form";
    }

    @PostMapping("/new-post")
    @ResponseBody
    public String uploadPost() {
        return "wow!";
    }
}

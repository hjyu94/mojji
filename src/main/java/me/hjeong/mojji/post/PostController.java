package me.hjeong.mojji.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.account.CurrentAccount;
import me.hjeong.mojji.category.CategoryRepository;
import me.hjeong.mojji.domain.Account;
import me.hjeong.mojji.domain.Category;
import me.hjeong.mojji.domain.Post;
import me.hjeong.mojji.domain.Station;
import me.hjeong.mojji.file.FileUploadService;
import me.hjeong.mojji.station.StationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CategoryRepository categoryRepository;
    private final StationRepository stationRepository;
    private final ObjectMapper objectMapper;

    @GetMapping("/new-post")
    public String newPost(@CurrentAccount Account account, Model model) throws JsonProcessingException {
        model.addAttribute(account);
        model.addAttribute("form", new PostForm());
        List<String> selectList = categoryRepository.findAll().stream().map(Category::getTitle).collect(Collectors.toList());
        model.addAttribute("selectList", selectList);

        List<String> whitelist = stationRepository.findAll().stream().map(Station::toString).collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(whitelist));
        return "post/form";
    }

    @PostMapping("/new-post")
    public String uploadPost(@CurrentAccount Account account, @Valid PostForm postForm, Errors errors, Model model) {
        if(errors.hasErrors()) {
            model.addAttribute(account);
            model.addAttribute("form", new PostForm());
            return "post/form";
        }
        Post savedPost = postService.createNewPost(postForm, account);
        return "redirect:/post/" + URLEncoder.encode(savedPost.getId().toString(), StandardCharsets.UTF_8);
    }

    @GetMapping("/post/{post-id}")
    public String getPost(@CurrentAccount Account account, @PathVariable("post-id") Long id, Model model) {
        model.addAttribute(account);
        return "post/view";
    }

}

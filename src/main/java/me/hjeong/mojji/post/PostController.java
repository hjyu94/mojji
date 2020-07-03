package me.hjeong.mojji.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hjeong.mojji.account.CurrentAccount;
import me.hjeong.mojji.category.CategoryRepository;
import me.hjeong.mojji.domain.Account;
import me.hjeong.mojji.domain.Category;
import me.hjeong.mojji.domain.Post;
import me.hjeong.mojji.domain.Station;
import me.hjeong.mojji.station.StationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
        putCategoryAndStationData(model);
        return "post/form";
    }

    @PostMapping("/new-post")
    public String uploadPost(@CurrentAccount Account account, @Valid PostForm postForm, Errors errors, Model model) throws IOException {
        if(errors.hasErrors()) {
            model.addAttribute(account);
            model.addAttribute("form", new PostForm());
            return "post/form";
        }
        Post savedPost = postService.createNewPost(postForm, account);
        return "redirect:/post/" + getEncodedURL(savedPost.getId());
    }

    private String getEncodedURL(Long id) {
        return URLEncoder.encode(id.toString(), StandardCharsets.UTF_8);
    }

    private void putCategoryAndStationData(Model model) throws JsonProcessingException {
        List<String> selectList = categoryRepository.findAll().stream().map(Category::getTitle).collect(Collectors.toList());
        model.addAttribute("selectList", selectList);

        List<String> whitelist = stationRepository.findAll().stream().map(Station::toString).collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(whitelist));
    }
}

package me.hjeong.mojji.module.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hjeong.mojji.module.account.CurrentAccount;
import me.hjeong.mojji.module.category.CategoryRepository;
import me.hjeong.mojji.infra.config.AppProperties;
import me.hjeong.mojji.module.account.Account;
import me.hjeong.mojji.module.category.Category;
import me.hjeong.mojji.module.station.Station;
import me.hjeong.mojji.module.post.repository.PostRepository;
import me.hjeong.mojji.module.station.StationRepository;
import me.hjeong.mojji.infra.util.FileUtils;
import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final StationRepository stationRepository;
    private final ObjectMapper objectMapper;
    private final AppProperties appProperties;
    private final ModelMapper modelMapper;

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
        return "redirect:" + savedPost.getEncodedViewURL();
    }

    @GetMapping("/post/{post-id}")
    public String getPost(@CurrentAccount Account account, @PathVariable("post-id") Long id, Model model) {
        Post post = postRepository.findById(id).orElseThrow(()-> new NoSuchElementException("해당하는 게시물을 찾을 수 없습니다."));
        if(!post.getSeller().equals(account)) {
            postService.lookCountUp(post);
        }
        model.addAttribute("account", account); // including null
        model.addAttribute(post);
        return "post/view";
    }

    @ResponseBody
    @GetMapping("/displayFile")
    public ResponseEntity<byte[]> displayFile(@RequestParam("post-id") Long id, @RequestParam String fileName) throws IOException {
        // 파일 내용 즉 이미지는 byte[]로 표현할 수 있다.

        log.info("************************* displayFile: {}, {}", id, fileName);

        Post post = postRepository.findById(id).orElseThrow(()-> {
            log.info("post {} 가 없습니다", id);
            return new NoSuchElementException("해당하는 게시물을 찾을 수 없습니다.");
        });
        String postUploadPath = FileUtils.getPostUploadPath(appProperties.getUploadPath(), post);

        InputStream in = null; // 파일을 읽어오자.
        ResponseEntity<byte[]> entity = null; // 파일을 출력하자.

        try {
            // 파일이 존재하지 않는다면 Not_Found 응답
            File file = new File(postUploadPath, fileName);
            if(!file.exists()) {
                log.info("{} 파일이 존재하지 않습니다.", file.getName());
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // 다운로드 할 파일을 읽어오자. FileInputStream은 파일을 쪽 빨아먹을 스트림을 파일에 꽂는 것
            // OutputStream 은 훅 내보낼 내용. 내용을 출력할 때 사용한다.
            in = new FileInputStream(file);

            String formatName = FileUtils.getFileExtension(fileName);
            MediaType mType = FileUtils.getMediaType(formatName);
            HttpHeaders headers = new HttpHeaders();
            if (mType != null) {
                headers.setContentType(mType); // MediaType.IMAGE_PNG 종류들
            } else {
                log.info("mType: {} 가 없습니다", mType);
            }
            // InputStream 을 ByteArray 로 변환하여 응답 body 에 실어서 보낸다.
            // (1,2,3) = (편지 내용, 편지 종류, 응답 코드)
            return new ResponseEntity<>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);

        } catch (Exception e) {
            log.info("error: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        } finally {
            if (in != null) {
                in.close(); // 반드시 스트림은 닫아줘야 한다. 안그러면 계속 누군가 써버리는 등의 일이 생길 수 있다.
            }
        }
    }

    @GetMapping("/post/{post-id}/edit")
    public String updatePost(@CurrentAccount Account account, @PathVariable("post-id") Long id, Model model) throws IOException {
        Post post = postRepository.findById(id).orElseThrow(()-> new NoSuchElementException("해당하는 게시물을 찾을 수 없습니다."));

        if(!post.isCreatedBy(account)) {
            throw new AccessDeniedException("수정 권한이 없습니다");
        }

        model.addAttribute(account);
        model.addAttribute(post);
        model.addAttribute("form", modelMapper.map(post, PostForm.class));
        model.addAttribute("alreadyUploadedFileNames", post.getImgFileNames());
        putCategoryAndStationData(model);

        return "post/edit";
    }

    @PostMapping("/post/{post-id}/edit")
    public String updatePostSubmit(@CurrentAccount Account account, @PathVariable("post-id") Long id
                                , @Valid PostForm postForm, Errors errors, Model model) throws IOException {
        Post post = postRepository.findById(id).orElseThrow(()-> new NoSuchElementException("해당하는 게시물을 찾을 수 없습니다."));
        if(!post.isCreatedBy(account)) {
            throw new AccessDeniedException("수정 권한이 없습니다");
        }
        if(errors.hasErrors()) {
            model.addAttribute(account);
            return "post/edit";
        }
        postService.updatePost(post, postForm);
        return "redirect:" + post.getEncodedViewURL();
    }

    @PostMapping("/post/{post-id}/sold")
    public String updateToSold(@CurrentAccount Account account, @PathVariable("post-id") Long id, Model model) {
        Post post = postRepository.findById(id).orElseThrow(()-> new NoSuchElementException("해당하는 게시물을 찾을 수 없습니다."));
        postService.updateToSold(post);
        return "redirect:" + post.getEncodedViewURL();
    }

    @DeleteMapping("/post/{post-id}")
    public String deletePost(@CurrentAccount Account account, @PathVariable("post-id") Long id) {
        Post post = postRepository.findById(id).orElseThrow(()-> new NoSuchElementException("해당하는 게시물을 찾을 수 없습니다."));
        if(!post.isCreatedBy(account)) {
            throw new AccessDeniedException("수정 권한이 없습니다");
        }
        postService.deletePost(post);
        return "index";
    }

    @GetMapping("/posts")
    public String getAllPosts(@CurrentAccount Account account, Model model
                            , @PageableDefault(size = 9, sort = "createdDateTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        model.addAttribute("posts", posts);
        return "post/list";
    }

    private void putCategoryAndStationData(Model model) throws JsonProcessingException {
        List<String> selectList = categoryRepository.findAll().stream().map(Category::getTitle).collect(Collectors.toList());
        model.addAttribute("selectList", selectList);

        List<String> whitelist = stationRepository.findAll().stream().map(Station::toString).collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(whitelist));
    }
}

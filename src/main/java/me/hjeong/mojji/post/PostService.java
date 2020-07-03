package me.hjeong.mojji.post;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.config.AppProperties;
import me.hjeong.mojji.domain.Account;
import me.hjeong.mojji.domain.Post;
import me.hjeong.mojji.util.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final AppProperties appProperties;

    public Post createNewPost(PostForm form, Account account) throws IOException {
        Post post = modelMapper.map(form, Post.class);
        post.setAccount(account);
        post.setCreatedDateTime(LocalDateTime.now());
        Post savedPost = postRepository.save(post);

        // upload image files
        String dirName = FileUtils.getPostUploadPath(appProperties.getUploadPath(), savedPost);
        List<String> imageFileNameList = FileUtils.storeFiles(form.getImages(), dirName);
        savedPost.setImgFileNames(imageFileNameList);

        return savedPost;
    }
}

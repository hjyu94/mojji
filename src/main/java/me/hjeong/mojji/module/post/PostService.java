package me.hjeong.mojji.module.post;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.infra.config.AppProperties;
import me.hjeong.mojji.module.account.Account;
import me.hjeong.mojji.infra.util.FileUtils;
import me.hjeong.mojji.module.post.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final AppProperties appProperties;

    public Post createNewPost(PostForm form, Account account) throws IOException {
        Post post = modelMapper.map(form, Post.class);
        post.setSeller(account);
        post.setCreatedDateTime(LocalDateTime.now());
        post.publishCreatedEvent();
        Post savedPost = postRepository.save(post);

        // upload image files
        uploadFiles(form.getImages(), savedPost);

        return savedPost;
    }

    public void updatePost(Post post, PostForm form) throws IOException {
        post.setTitle(form.getTitle());
        post.setStations(form.getStations());
        post.setCategory(form.getCategory());
        post.setBody(form.getBody());
        post.setPrice(form.getPrice());

        // delete file process
        deleteFiles(form.getDeleteFileNames(), post);

        // upload file process
        uploadFiles(form.getImages(), post);
    }

    private void deleteFiles(Set<String> fileNames, Post post) {
        if (null != fileNames) {
            String dirName = FileUtils.getPostUploadPath(appProperties.getUploadPath(), post);
            FileUtils.deleteFiles(fileNames, dirName);
            fileNames.forEach(deleteFileName -> {
                post.getImgFileNames().remove(deleteFileName);
            });
        }
    }

    private void uploadFiles(Set<MultipartFile> fileNames, Post post) throws IOException {
        if (null != fileNames) {
            String dirName = FileUtils.getPostUploadPath(appProperties.getUploadPath(), post);
            List<String> imageFileNameList = FileUtils.storeFiles(fileNames, dirName);
            imageFileNameList.forEach(fileName -> {
                post.getImgFileNames().add(fileName);
            });
        }
    }

    public void deletePost(Post post) {
        String postUploadPath = FileUtils.getPostUploadPath(appProperties.getUploadPath(), post);
        FileUtils.deleteFolder(postUploadPath);

        postRepository.delete(post);
    }

    public void lookCountUp(Post post) {
        post.setLookCount(post.getLookCount() + 1);
    }

    public void sendingLetterCountUp(Post post) {
        post.setLetterCount(post.getLetterCount() + 1);
    }

    public void updateToSold(Post post) {
        post.setSold(true);
    }
}

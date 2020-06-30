package me.hjeong.mojji.post;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.category.CategoryRepository;
import me.hjeong.mojji.domain.Account;
import me.hjeong.mojji.domain.Post;
import me.hjeong.mojji.domain.Station;
import me.hjeong.mojji.infra.FileUploadService;
import me.hjeong.mojji.station.StationRepository;
import me.hjeong.mojji.station.StationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final FileUploadService fileUploadService;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final StationService stationService;
    private final StationRepository stationRepository;

    public Post createNewPost(PostForm form, Account account) {
        // save in repository
        Post post = modelMapper.map(form, Post.class);
        Post savedPost = postRepository.save(post);

        // transfer data form form to entity
        savedPost.setAccount(account);
        savedPost.setCreatedDateTime(LocalDateTime.now());
        Set<Station> stationSet = Arrays.stream(form.getStations().split(","))
                .map(stationService::getStationByString).collect(Collectors.toSet());
        savedPost.setStations(stationSet);
        savedPost.setCategory(categoryRepository.findByTitle(form.getCategory()));

        // upload image files
        Set<MultipartFile> files = form.getFiles();
        Set<String> urlList = fileUploadService.restoreList(savedPost.getId().toString(), files);
        savedPost.setImages(urlList);

        return savedPost;
    }
}

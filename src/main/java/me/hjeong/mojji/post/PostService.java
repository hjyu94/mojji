package me.hjeong.mojji.post;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.category.CategoryRepository;
import me.hjeong.mojji.domain.Account;
import me.hjeong.mojji.domain.Category;
import me.hjeong.mojji.domain.Post;
import me.hjeong.mojji.domain.Station;
import me.hjeong.mojji.file.FileUploadService;
import me.hjeong.mojji.station.StationRepository;
import me.hjeong.mojji.station.StationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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
        Post post = modelMapper.map(form, Post.class);
        post.setAccount(account);
        post.setCreatedDateTime(LocalDateTime.now());
        Set<Station> stationSet = Arrays.stream(form.getStations().split(","))
                .map(stationService::getStationByString).collect(Collectors.toSet());
        post.setStations(stationSet);
        post.setCategory(categoryRepository.findByTitle(form.getCategory()));

        Set<MultipartFile> files = form.getFiles();
        Set<String> urlList = fileUploadService.restoreList(files);
        post.setImages(urlList);

        return postRepository.save(post);
    }
}

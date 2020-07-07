package me.hjeong.mojji;

import me.hjeong.mojji.account.AccountRepository;
import me.hjeong.mojji.category.CategoryRepository;
import me.hjeong.mojji.config.AppProperties;
import me.hjeong.mojji.domain.Account;
import me.hjeong.mojji.domain.Post;
import me.hjeong.mojji.post.PostRepository;
import me.hjeong.mojji.station.StationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

//@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    PostRepository postRepository;

    @Autowired
    AppProperties appProperties;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for(int i=0; i<100; i++) {
            Post post = new Post();
            post.setTitle("임의 게시물 " + i);
            post.setBody("내용");
            Long stationId = (long) (Math.abs(Math.random() * 500) + 23); // 0~0.9999 -> 0~499.999 -> 23~522.999 -> 23 ~ 522
            post.getStations().add(stationRepository.findById(stationId).orElse(null));
            post.setCategory(categoryRepository.findById((long) 777).orElse(null));
            post.setPrice((int) Math.abs(Math.random() * 100000));
            Account account = accountRepository.findByNickname("12345678");
            post.setCreatedDateTime(LocalDateTime.now());
            post.setAccount(account);
            post.getImgFileNames().add("KakaoTalk_20200622_100949064.jpg");
            postRepository.save(post);
        }
    }
}

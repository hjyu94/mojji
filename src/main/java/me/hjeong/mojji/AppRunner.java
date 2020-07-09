package me.hjeong.mojji;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.account.AccountRepository;
import me.hjeong.mojji.category.CategoryRepository;
import me.hjeong.mojji.config.AppProperties;
import me.hjeong.mojji.domain.Account;
import me.hjeong.mojji.domain.Post;
import me.hjeong.mojji.post.PostRepository;
import me.hjeong.mojji.station.StationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.time.LocalDateTime;

//@Component
@RequiredArgsConstructor
public class AppRunner implements ApplicationRunner {

    private final PostRepository postRepository;
    private final AccountRepository accountRepository;
    private final StationRepository stationRepository;
    private final CategoryRepository categoryRepository;

    private final long stationStartId = 24;
    private final long stationEndId = 749;
    private final long categoryStartId = 1;
    private final long categoryEndId = 23;
    private final String nickname = "1234";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for(int i=0; i<100; i++) {
            Post post = new Post();
            post.setTitle("임의 게시물 " + i);
            post.setBody("내용");

            Long stationId = (long) (Math.abs(Math.random() * (stationEndId - stationStartId + 1)) + stationStartId); // 0~0.999 -> 0~725.999 -> 0~725 -> 24~749
            post.getStations().add(stationRepository.findById(stationId).orElse(null));

            Long categoryId = (long) (Math.abs(Math.random() * (categoryEndId - categoryStartId + 1)) + categoryStartId);
            post.setCategory(categoryRepository.findById(categoryId).orElse(null));

            post.setPrice((int) Math.abs(Math.random() * 100) * 1000);
            Account account = accountRepository.findByNickname(nickname);
            post.setCreatedDateTime(LocalDateTime.now());
            post.setSeller(account);
            post.getImgFileNames().add("test.jpg");
            postRepository.save(post);
        }
    }
}

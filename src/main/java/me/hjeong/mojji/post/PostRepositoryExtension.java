package me.hjeong.mojji.post;

import me.hjeong.mojji.domain.Account;
import me.hjeong.mojji.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryExtension {
    Page<Post> findByKeywords(Pageable pageable, List<String> keywords);
    List<Post> findPostsBySellerAndIsSold(Account account, boolean isSold);
}

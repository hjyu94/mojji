package me.hjeong.mojji.module.post.repository;

import me.hjeong.mojji.module.account.Account;
import me.hjeong.mojji.module.category.Category;
import me.hjeong.mojji.module.post.Post;
import me.hjeong.mojji.module.station.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface PostRepositoryExtension {
    Page<Post> findByKeywords(Pageable pageable, List<String> keywords);
    List<Post> findPostsBySellerAndIsSold(Account account, boolean isSold);
    List<Post> findFirst9ByCategoriesOrderByCreatedDateTimeDesc(Set<Category> categories);
    List<Post> findFirst9ByStationsOrderByCreatedDateTimeDesc(Set<Station> stations);
}

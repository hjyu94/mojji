package me.hjeong.mojji.category;

import me.hjeong.mojji.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByTitle(String title);
}

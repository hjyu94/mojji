package me.hjeong.mojji.category;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.domain.Account;
import me.hjeong.mojji.domain.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryFactory {

    @Autowired CategoryRepository categoryRepository;

    public Category createCategory(String title) {
        Category category = Category.builder()
                .title(title)
                .build();
        return categoryRepository.save(category);
    }

}
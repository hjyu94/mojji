package me.hjeong.mojji.factory;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.category.CategoryRepository;
import me.hjeong.mojji.domain.Account;
import me.hjeong.mojji.domain.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryFactory {

    @Autowired
    CategoryRepository categoryRepository;

    public Category createCategory(String title) {
        Category category = Category.builder()
                .title(title)
                .build();
        return categoryRepository.save(category);
    }

    public Category getOne() {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()) {
            return createCategory("카테고리");
        } else {
            return categories.get(0);
        }
    }
}
package me.hjeong.mojji.infra.factory;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.module.category.CategoryRepository;
import me.hjeong.mojji.module.category.Category;
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
package me.hjeong.mojji.infra.formatter;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.module.category.CategoryRepository;
import me.hjeong.mojji.module.category.Category;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CategoryFormatter implements Formatter<Category> {

    private final CategoryRepository categoryRepository;

    @Override
    public Category parse(String s, Locale locale) throws ParseException {
        return categoryRepository.findByTitle(s);
    }

    @Override
    public String print(Category category, Locale locale) {
        return category.getTitle();
    }
}

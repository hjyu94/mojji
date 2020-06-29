package me.hjeong.mojji.category;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.domain.Category;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ResourceLoader resourceLoader;

    @PostConstruct
    public void initZoneData() throws IOException {
        if (categoryRepository.count() == 0) {
            try (InputStream resource = resourceLoader.getResource("classpath:category.csv").getInputStream()) {
                List<String> strCategory = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
                List<Category> categoryList = strCategory.stream().map(line -> Category.builder().title(line).build()).collect(Collectors.toList());
                categoryRepository.saveAll(categoryList);
            }
        }
    }
}

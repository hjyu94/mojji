package me.hjeong.mojji.post;

import lombok.Data;
import me.hjeong.mojji.domain.Category;
import me.hjeong.mojji.domain.Station;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Data
public class PostForm {

    private String title;
    private Integer price;
    private String body;
    private Category category;
    private Set<Station> stations;
    private Set<MultipartFile> images;
    private Set<String> deleteFileNames; // can be null

}

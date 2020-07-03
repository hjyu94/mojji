package me.hjeong.mojji.post;

import lombok.Data;
import me.hjeong.mojji.domain.Category;
import me.hjeong.mojji.domain.Station;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PostForm {

    private String title;
    private Integer price;
    private String body;
    private Category category;
    private List<Station> stations;
    private List<MultipartFile> images;
    private List<String> deleteFileNames; // can be null

}

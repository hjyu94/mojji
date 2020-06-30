package me.hjeong.mojji.post;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Data
public class PostForm {

    private String category;
    private String title;
    private String body;
    private String stations;
    private Set<MultipartFile> files;

}

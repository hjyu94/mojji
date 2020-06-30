package me.hjeong.mojji.post;

import lombok.Data;

import java.util.Set;

@Data
public class PostForm {

    private String title;
    private String body;
    private Set<String> images;

}

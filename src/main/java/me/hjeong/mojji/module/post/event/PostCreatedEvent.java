package me.hjeong.mojji.module.post.event;

import lombok.Getter;
import me.hjeong.mojji.module.post.Post;

@Getter
public class PostCreatedEvent {

    private Post post;

    public PostCreatedEvent(Post post) {
        this.post = post;
    }

}

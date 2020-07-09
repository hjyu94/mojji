package me.hjeong.mojji.post.event;

import lombok.Getter;
import me.hjeong.mojji.domain.Post;

@Getter
public class PostCreatedEvent {

    private Post post;

    public PostCreatedEvent(Post post) {
        this.post = post;
    }

}

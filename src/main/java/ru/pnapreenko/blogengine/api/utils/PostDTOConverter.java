package ru.pnapreenko.blogengine.api.utils;

import ru.pnapreenko.blogengine.model.Post;
import ru.pnapreenko.blogengine.model.dto.post.PostDTO;

public class PostDTOConverter {
    private PostDTOConverter() {
    }

    public static PostDTO getConversion(Post post) {
        return new PostDTO(post);
    }
}


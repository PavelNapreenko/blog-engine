package ru.pnapreenko.blogengine.api.utils;

import ru.pnapreenko.blogengine.model.PostComment;
import ru.pnapreenko.blogengine.model.dto.post.PostCommentDTO;

public class CommentDTOConverter {
    private CommentDTOConverter() {
    }

    public static PostCommentDTO getConversion(PostComment comment) {
        return new PostCommentDTO(comment);
    }
}


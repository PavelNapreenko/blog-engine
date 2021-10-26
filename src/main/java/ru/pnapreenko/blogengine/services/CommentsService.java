package ru.pnapreenko.blogengine.services;

import org.springframework.stereotype.Service;
import ru.pnapreenko.blogengine.model.Post;
import ru.pnapreenko.blogengine.model.PostComment;
import ru.pnapreenko.blogengine.model.User;
import ru.pnapreenko.blogengine.repositories.CommentsRepository;

@Service
public class CommentsService {

    private final CommentsRepository commentsRepository;

    public CommentsService(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }

    public int addComment(User user, PostComment parentComment, Post post, String text) {

        PostComment newComment = new PostComment();

        newComment.setParentComment(parentComment);
        newComment.setUser(user);
        newComment.setPost(post);
        newComment.setText(text);

        PostComment savedComment = commentsRepository.save(newComment);
        return savedComment.getId();
    }
}
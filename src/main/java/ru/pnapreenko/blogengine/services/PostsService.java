package ru.pnapreenko.blogengine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.pnapreenko.blogengine.api.responses.APIResponse;
import ru.pnapreenko.blogengine.api.utils.MainPagePostsOffset;
import ru.pnapreenko.blogengine.enums.PostMode;
import ru.pnapreenko.blogengine.model.dto.post.ListPostsDTO;
import ru.pnapreenko.blogengine.model.dto.post.PostDTO;
import ru.pnapreenko.blogengine.repositories.PostsRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Autowired
    public PostsService(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    }

    public ResponseEntity<?> getPosts(int offset, int limit, String postMode) {

        final Instant now = Instant.now();
        final PostMode mode;

        Sort sort = Sort.by(Sort.Direction.DESC, "time");

        try {
            mode = PostMode.getByName(postMode);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(APIResponse.error(e.getMessage()));
        }

        switch (mode) {
            /* сортировать по дате публикации, выводить сначала старые */
            case EARLY:
                sort = Sort.by(Sort.Direction.ASC, "time");
                break;

            /* сортировать по убыванию количества лайков */
            case BEST:
                sort = Sort.by(Sort.Direction.DESC, "like_count");
                break;

            /* сортировать по убыванию количества комментариев */
            case POPULAR:
                /* сортировать по дате публикации, выводить сначала новые */
            case RECENT:
            default:
                break;
        }

        Pageable pageable = new MainPagePostsOffset(offset, limit, sort);

        Page<PostDTO> posts = postsRepository.findAllPosts(now, pageable);

        if (mode == PostMode.POPULAR) {
            final List<PostDTO> p = new ArrayList<>(posts.getContent());
            Collections.sort(p);
            posts = new PageImpl<>(p);
        }
        return ResponseEntity.ok(new ListPostsDTO(posts));
    }

//    public ResponseEntity<?> searchPosts(int offset, int limit, String query) {
//        final int minimalQueryLength = 3;
//        if (query == null || query.length() < minimalQueryLength)
//            return ResponseEntity.badRequest().body(APIResponse.error(String.format("Параметр 'query' должен быть " +
//        "не менее %d символов.", minimalQueryLength)));
//
//        Sort sort = Sort.by(Sort.Direction.DESC, "time");
//        Pageable pageable = new MainPagePostsOffset(offset, limit, sort);
//        List<PostDTO> queryPosts = new ArrayList<>();
//        for (PostDTO p: postList) {
//            String s = p.getText().toLowerCase();
//            if (s.contains(query.toLowerCase())) {
//                queryPosts.add(p);
//            }
//        }
//        Page<PostDTO> posts = new PageImpl<>(queryPosts, pageable, queryPosts.size());
//        return ResponseEntity.ok(new ListPostsDTO(posts));
//    }
}

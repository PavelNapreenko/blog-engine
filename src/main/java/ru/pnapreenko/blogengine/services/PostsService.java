package ru.pnapreenko.blogengine.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.pnapreenko.blogengine.api.responses.APIResponse;
import ru.pnapreenko.blogengine.api.utils.MainPagePostsOffset;
import ru.pnapreenko.blogengine.enums.ModerationStatus;
import ru.pnapreenko.blogengine.enums.PostMode;
import ru.pnapreenko.blogengine.model.Post;
import ru.pnapreenko.blogengine.model.User;
import ru.pnapreenko.blogengine.model.dto.ListPostsDTO;
import ru.pnapreenko.blogengine.model.dto.PostDTO;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PostsService {

    private final List<PostDTO> postList = new ArrayList<>();

    public ResponseEntity<?> getPosts(int offset, int limit, String postMode) {

        PostMode mode = PostMode.getByName(postMode);
        Sort sort = Sort.by(Sort.Direction.DESC, "time");

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

        for(int i = 1; i < 21; i++) {
            PostDTO postForPage = new PostDTO(getTestPost(i), i+2, i+1);
            postList.add(postForPage);
        }

        Page<PostDTO> posts = new PageImpl<> (postList, pageable, postList.size());

        if (mode == PostMode.POPULAR) {
            final List<PostDTO> p = new ArrayList<>(posts.getContent());
            Collections.sort(p);
            posts = new PageImpl<>(p);
        }

        return ResponseEntity.ok(new ListPostsDTO(posts));
    }

    public ResponseEntity<?> searchPosts(int offset, int limit, String query) {

        if (query == null || query.length() < 3)
            return ResponseEntity.badRequest().body(APIResponse.error(String.format("Параметр 'query' должен быть " +
        "не менее %d символов.", 3)));

        Sort sort = Sort.by(Sort.Direction.DESC, "time");
        Pageable pageable = new MainPagePostsOffset(offset, limit, sort);
        List<PostDTO> queryPosts = new ArrayList<>();
        for (PostDTO p: postList) {
            String s = p.getText().toLowerCase();
            if (s.contains(query.toLowerCase())) {
                queryPosts.add(p);
            }
        }
        Page<PostDTO> posts = new PageImpl<>(queryPosts, pageable, queryPosts.size());
        return ResponseEntity.ok(new ListPostsDTO(posts));
    }

    private Post getTestPost(int i) {
        User user = new User();
        user.setId(i);
        user.setName("Иван IV Грозный");
        Post post = new Post();
        post.setId(i);
        post.setModerationStatus(ModerationStatus.ACCEPTED);
        post.setActive(true);
        post.setAuthor(user);
        post.setText("BLA bla bla bla");
        post.setTitle("Title");
        post.setViewCount(i+1);
        post.setTime(Instant.now());
        return post;
    }

    public List<PostDTO> getPostList() {
        return new ArrayList<>(postList);
    }
}

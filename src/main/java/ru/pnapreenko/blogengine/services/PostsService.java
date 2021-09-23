package ru.pnapreenko.blogengine.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.pnapreenko.blogengine.api.utils.MainPagePostsOffset;
import ru.pnapreenko.blogengine.enums.ModerationStatus;
import ru.pnapreenko.blogengine.enums.PostMode;
import ru.pnapreenko.blogengine.model.Post;
import ru.pnapreenko.blogengine.model.PostComment;
import ru.pnapreenko.blogengine.model.User;
import ru.pnapreenko.blogengine.model.dto.ListPostsDTO;
import ru.pnapreenko.blogengine.model.dto.PostDTO;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PostsService {

    private final List<PostDTO> postList = new ArrayList<>();

    public ResponseEntity<?> getPosts(int offset, int limit, String postMode) {

        final Comparator<PostDTO> comparatorEarly = Comparator.comparing(PostDTO::getTimestamp);
        final Comparator<PostDTO> comparatorBest = Comparator.comparing(PostDTO::getLikeCount);
        final Comparator<PostDTO> comparatorPopular = Comparator.comparing(PostDTO::getCommentCount);

        postList.sort(comparatorEarly.reversed());

        PostMode mode = PostMode.getByName(postMode);
        Sort sort = Sort.by(Sort.Direction.DESC, "timestamp");

        switch (mode) {
            /* сортировать по дате публикации, выводить сначала старые */
            case EARLY:
                //sort = Sort.by(Sort.Direction.ASC, "time");
                postList.sort(comparatorEarly);
                break;

            /* сортировать по убыванию количества лайков */
            case BEST:
                //sort = Sort.by(Sort.Direction.DESC, "like_count");
                postList.sort(comparatorBest.reversed());
                break;

            /* сортировать по убыванию количества комментариев */
            case POPULAR:
                postList.sort(comparatorPopular.reversed());
                /* сортировать по дате публикации, выводить сначала новые */
            case RECENT:
            default:
                break;
        }

        Pageable pageable = new MainPagePostsOffset(offset, limit, sort);

        for(int i = 1; i < 15; i++) {
            Random r = new Random();
            Instant oneYearAgo = Instant.now().minus(Duration.ofDays(365));
            Instant now = Instant.now();
            Instant random = between(oneYearAgo, now);

            PostDTO postForPage = new PostDTO(getTestPost(i), r.nextInt(30), r.nextInt(30));
            postForPage.setTimestamp(random.getEpochSecond());
            postList.add(postForPage);
        }

        Page<PostDTO> posts = new PageImpl<> (postList, pageable, postList.size());

//        if (mode == PostMode.POPULAR) {
//            final List<PostDTO> p = new ArrayList<>(posts.getContent());
//            Collections.sort(p);
//            posts = new PageImpl<>(p);
//        }

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

    private Post getTestPost(int i) {
        Random r = new Random();
        User user = new User();
        user.setId(i);
        user.setName("Иван IV Грозный");
        PostComment postComment = new PostComment();
        postComment.setText("Bla bla");
        Post post = new Post();
        post.setId(i);
        post.setModerationStatus(ModerationStatus.ACCEPTED);
        post.setActive(true);
        post.setAuthor(user);
        post.setText("BLA bla bla bla");
        post.setTitle("Title");
        post.setViewCount(r.nextInt(50));
        post.setTime(Instant.now());
        post.setComments(setComments(postComment, r.nextInt(20)));
        return post;
    }

    private Set<PostComment> setComments(PostComment comment, int size) {
        Set<PostComment> setForPost = new HashSet<>();
        for (int i = 0; i < size; i++ ) {
            setForPost.add(comment);
        }
        return setForPost;
    }
    private static Instant between(Instant startInclusive, Instant endExclusive) {
        long startSeconds = startInclusive.getEpochSecond();
        long endSeconds = endExclusive.getEpochSecond();
        long random = ThreadLocalRandom
                .current()
                .nextLong(startSeconds, endSeconds);
        return Instant.ofEpochSecond(random);
    }

    public List<PostDTO> getPostList() {
        return new ArrayList<>(postList);
    }
}

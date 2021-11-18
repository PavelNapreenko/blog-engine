package ru.pnapreenko.blogengine.model.dto.post;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import ru.pnapreenko.blogengine.api.utils.JsonViews;
import ru.pnapreenko.blogengine.model.Post;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostIdDTO {


    @JsonView({JsonViews.IdName.class, JsonViews.EntityId.class})
    private int id;

    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private String title;

    @JsonView(JsonViews.IdName.class)
    private boolean isActive;

    @JsonView(JsonViews.EntityIdName.class)
    private String text;

    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private long timestamp;

    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private PostAuthorDTO user;

    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private int viewCount;

    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private long likeCount;

    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private long dislikeCount;

    @JsonView(JsonViews.EntityIdName.class)
    private List<String> tags;

    @JsonView(JsonViews.EntityIdName.class)
    private List<PostCommentDTO> comments;

    private Instant date;

    public PostIdDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.text = Jsoup.parse(post.getText()).text();
        this.timestamp = post.getTime().getEpochSecond();
        this.user = new PostAuthorDTO(post.getAuthor().getId(), post.getAuthor().getName());
        this.viewCount = post.getViewCount();
        this.likeCount = getLikeCount(post);
        this.dislikeCount = getDislikeCount(post);
        this.date = post.getTime();
        this.comments = new ArrayList<>();
    }

    private long getLikeCount(Post post) {
        return post.getVotes().stream().filter(postVote -> postVote.getValue() > 0).count();
    }

    private long getDislikeCount(Post post) {
        return post.getVotes().stream().filter(postVote -> postVote.getValue() < 0).count();
    }

}

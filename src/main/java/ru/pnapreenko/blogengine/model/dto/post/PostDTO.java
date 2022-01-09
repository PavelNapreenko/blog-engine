package ru.pnapreenko.blogengine.model.dto.post;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import ru.pnapreenko.blogengine.api.utils.JsonViews;
import ru.pnapreenko.blogengine.model.Post;
import ru.pnapreenko.blogengine.model.PostComment;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO implements Comparable<PostDTO> {

    @JsonView({JsonViews.IdName.class, JsonViews.EntityId.class})
    private int id;

    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private String title;

    @JsonView(JsonViews.IdName.class)
    private String announce;

    @JsonView(JsonViews.EntityIdName.class)
    private String text;

    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private long timestamp;

    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private PostAuthorDTO user;

    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private int viewCount;

    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private int commentCount;

    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private long likeCount;

    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private long dislikeCount;

    @JsonView(JsonViews.EntityIdName.class)
    private List<PostComment> comments;

    private Instant date;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.text = post.getText();
        this.announce = getAnnounce(Jsoup.parse(post.getText()).text());
        this.timestamp = post.getTime().getEpochSecond();
        this.user = new PostAuthorDTO(post.getAuthor().getId(), post.getAuthor().getName());
        this.viewCount = post.getViewCount();
        this.commentCount = post.getComments().size();
        this.likeCount = getLikeCount(post);
        this.dislikeCount = getDislikeCount(post);
        this.date = post.getTime();
        this.comments = new ArrayList<>();
    }

    @Override
    public int compareTo(PostDTO o) {
        int result = o.getCommentCount() - this.getCommentCount();
        if (result == 0) result = o.getDate().compareTo(this.getDate());
        return result;
    }

    private String getAnnounce(String text) {
        text = text.substring(0, 150).concat("...");
        return text;
    }

    private long getLikeCount(Post post) {
        return post.getVotes().stream().filter(postVote -> postVote.getValue() > 0).count();
    }

    private long getDislikeCount(Post post) {
        return post.getVotes().stream().filter(postVote -> postVote.getValue() < 0).count();
    }

}

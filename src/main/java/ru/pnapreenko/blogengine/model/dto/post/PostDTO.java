package ru.pnapreenko.blogengine.model.dto.post;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.Jsoup;
import ru.pnapreenko.blogengine.api.utils.JsonViews;
import ru.pnapreenko.blogengine.model.Post;
import ru.pnapreenko.blogengine.model.PostComment;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class PostDTO implements Comparable<PostDTO> {

    @Getter
    @JsonView({JsonViews.IdName.class, JsonViews.EntityId.class})
    private final int id;

    @Getter
    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private final String title;

    @Getter
    @JsonView(JsonViews.IdName.class)
    private final String announce;

    @Getter
    @JsonView(JsonViews.EntityIdName.class)
    private final String text;

    @Getter @Setter
    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private long timestamp;

    @Getter
    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private final PostAuthorDTO user;

    @Getter
    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private final int viewCount;

    @Getter
    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private final int commentCount;

    @Getter @Setter
    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private long likeCount;

    @Getter @Setter
    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private long dislikeCount;

    @Getter @Setter
    @JsonView(JsonViews.EntityIdName.class)
    private List<String> tags;

    @Getter @Setter
    @JsonView(JsonViews.EntityIdName.class)
    private List<PostComment> comments;

    @Getter
    private final Instant date;

    public PostDTO(Post post) {
        this(post, 0, 0);
    }

    public PostDTO(Post post, long likeCount, long dislikeCount) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.text = post.getText();
        this.announce = Jsoup.parse(getAnnounce(post.getText())).text();
        this.timestamp = post.getTime().getEpochSecond();
        this.user = new PostAuthorDTO(post.getAuthor().getId(), post.getAuthor().getName());
        this.viewCount = post.getViewCount();

        this.commentCount = post.getComments().size();
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;

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
        text = text.substring(0,149).concat("...");
        return text;
    }
}

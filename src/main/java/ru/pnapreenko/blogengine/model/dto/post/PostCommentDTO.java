package ru.pnapreenko.blogengine.model.dto.post;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pnapreenko.blogengine.api.utils.JsonViews;
import ru.pnapreenko.blogengine.model.PostComment;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCommentDTO {

    @JsonView({JsonViews.IdName.class, JsonViews.EntityId.class})
    private int id;

    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private long timestamp;

    @JsonView(JsonViews.EntityIdName.class)
    private String text;

    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private CommentAuthorDTO user;

    public PostCommentDTO(PostComment comment) {
        this.id = comment.getId();
        this.timestamp = comment.getTime().getEpochSecond();
        this.text = comment.getText();
        this.user = new CommentAuthorDTO(comment.getUser().getId(), comment.getUser().getName(), comment.getUser().getPhoto());
    }
}

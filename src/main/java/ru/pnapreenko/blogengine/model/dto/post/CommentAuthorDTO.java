package ru.pnapreenko.blogengine.model.dto.post;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pnapreenko.blogengine.api.utils.JsonViews;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentAuthorDTO {
    @JsonView({JsonViews.Id.class})
    private int id;

    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private String name;

    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private String photo;

}
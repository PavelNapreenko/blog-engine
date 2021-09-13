package ru.pnapreenko.blogengine.model.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.pnapreenko.blogengine.api.utils.JsonViews;

@Data
@NoArgsConstructor
@ToString
public class UserAuthenticationDTO {
    
    @JsonView(JsonViews.IdName.class)
    private int id;

    @JsonView(JsonViews.IdName.class)
    private String name;

    @JsonView(JsonViews.IdName.class)
    private String photo;

    @JsonView(JsonViews.IdName.class)
    private String email;

    @JsonView(JsonViews.IdName.class)
    private boolean moderation;

    @JsonView(JsonViews.IdName.class)
    private long moderationCount;

    @JsonView(JsonViews.IdName.class)
    private boolean settings;

    public UserAuthenticationDTO(int id, String name, String photo, String email) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.email = email;
        this.moderation = false;
        this.moderationCount = 0;
        this.settings = false;
    }

    public void setUserModeratorStatus(int moderationCount) {
        this.moderation = true;
        this.moderationCount = moderationCount;
        this.settings = true;
    }
}

package ru.pnapreenko.blogengine.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NaturalId;
import ru.pnapreenko.blogengine.api.utils.JsonViews;
import ru.pnapreenko.blogengine.enums.Role;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor(force = true)
@EqualsAndHashCode(callSuper = true, of = {"email"})
@ToString(callSuper = true, of = {"name"})
public class User extends AbstractEntity {

    @Column(name = "is_moderator", nullable = false)
    private boolean isModerator;

    @NotNull
    @Column(name = "reg_time", nullable = false)
    private Instant regTime;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false)
    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private String name;

    @NaturalId(mutable = true)
    @Email
    @NotBlank
    @Size(max = 255)
    @Column(nullable = false)
    private String email;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false)
    private String password;

    @Size(max = 255)
    private String code;

    @Column(columnDefinition = "TEXT")
    @JsonView(JsonViews.EntityIdName.class)
    private String photo;

    @NotNull
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, orphanRemoval = true)
    private final Set<Post> posts = new HashSet<>();

    @NotNull
    @OneToMany(mappedBy = "moderatedBy", fetch = FetchType.LAZY, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private final Set<Post> moderatedPosts = new HashSet<>();

    @NotNull
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    private final Set<PostComment> comments = new HashSet<>();

    @NotNull
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    private final Set<PostVote> votes = new HashSet<>();

    public Role getRole() {
        return isModerator ? Role.MODERATOR : Role.USER;
    }
}

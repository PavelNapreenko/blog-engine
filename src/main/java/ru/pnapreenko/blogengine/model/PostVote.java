package ru.pnapreenko.blogengine.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Table(name = "post_votes")
@Data
@NoArgsConstructor(force = true)
@EqualsAndHashCode(callSuper = true, of = {"time", "value"})
@ToString(callSuper = true, of = {"value"})
public class PostVote extends AbstractEntity {
    public PostVote(@NotNull User user, @NotNull Post post) {
        this.user = user;
        this.post = post;
    }

    public PostVote(@NotNull User user, @NotNull Post post, @NotNull Instant time) {
        this(user, post);
        this.time = time;
    }

    @NotNull
    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @NotNull
    @Column(nullable = false)
    private Instant time = Instant.now();

    @Column(nullable = false)
    private byte value;

    public void like() {
        this.value = 1;
    }

    public void dislike() {
        this.value = -1;
    }
}

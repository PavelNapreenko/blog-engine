package ru.pnapreenko.blogengine.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.pnapreenko.blogengine.api.utils.JsonViews;

import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "post_comments")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, of = {"text", "time"})
@ToString(callSuper = true, of = {"text", "user", "time"})
public class PostComment extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private PostComment parentComment;

    @NotNull
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(mappedBy = "parentComment", fetch = FetchType.LAZY, orphanRemoval = true)
    private final Set<PostComment> childComments = new HashSet<>();

    @NotNull
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    @JsonView(JsonViews.EntityIdName.class)
    private User user;

    @NotNull
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false, updatable = false)
    private Post post;

    @NotNull
    @Column(nullable = false)
    private Instant time = Instant.now();

    @NotBlank
    @Column(columnDefinition = "TEXT", nullable = false)
    @JsonView(JsonViews.EntityIdName.class)
    private String text;
    }
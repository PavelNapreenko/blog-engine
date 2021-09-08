package ru.pnapreenko.blogengine.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "post_comments")
@Data
@NoArgsConstructor(force = true)
@EqualsAndHashCode(callSuper = true, of = {"text", "time"})
@ToString(callSuper = true, of = {"text", "user", "time"})
public class PostComment extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private PostComment parentComment;

    @NotNull
    @OneToMany(mappedBy = "parentComment", fetch = FetchType.LAZY, orphanRemoval = true)
    private final Set<PostComment> childComments = new HashSet<>();

    @NotNull
    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false)
    private User user;

    @NotNull
    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false, updatable = false)
    private Post post;

    @NotNull
    @Column(nullable = false)
    private Date time;

    @NotBlank
    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    }
package ru.pnapreenko.blogengine.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "posts")
@ToString
@Getter
@Setter
@RequiredArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer postId;

    @Column(name = "is_active", nullable = false)
    private Short isActive;

    @Column(name = "moderation_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ModerationStatus moderationStatus;

    @Column(name = "moderator_id")
    private Integer moderatorId;

    @Column(name = "user_id", nullable = false, insertable = false, updatable = false)
    private Integer postUserId;

    @Column(name = "time", nullable = false)
    private Date postPublicationTime;

    @Column(name = "title", nullable = false)
    private String postTitle;

    @Column(name = "text", nullable = false)
    private String postText;

    @Column(name = "view_count", nullable = false)
    private Integer postViewCount;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "user"))
    private User user;

    @ToString.Exclude
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "tag2post",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;

    @ToString.Exclude
    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<PostComment> postComments;

    @ToString.Exclude
    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<PostVote> postVotes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return getPostId().equals(post.getPostId()) && getIsActive().equals(post.getIsActive()) && getModerationStatus() == post.getModerationStatus()
                && Objects.equals(getModeratorId(), post.getModeratorId()) && getPostUserId().equals(post.getPostUserId())
                && getPostPublicationTime().equals(post.getPostPublicationTime()) && getPostTitle().equals(post.getPostTitle())
                && getPostText().equals(post.getPostText()) && getPostViewCount().equals(post.getPostViewCount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPostId(), getIsActive(), getModerationStatus(), getModeratorId(), getPostUserId(), getPostPublicationTime(),
                getPostTitle(), getPostText(), getPostViewCount());
    }
}

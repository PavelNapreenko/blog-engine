package ru.pnapreenko.blogengine.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "post_comments")
@ToString
@Getter
@Setter
@RequiredArgsConstructor
public class PostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer commentId;

    @Column(name = "parent_id")
    private Integer parentCommentId;

    @Column(name = "post_id", nullable = false, insertable = false, updatable = false)
    private Integer postCommentId;

    @Column(name = "user_id", nullable = false, insertable = false, updatable = false)
    private Integer userCommentId;

    @Column(name = "time", nullable = false)
    private Date commentTime;

    @Column(name = "text", nullable = false)
    private String commentText;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "post_id", referencedColumnName = "id",
    foreignKey = @ForeignKey(name = "post"))
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id",
    foreignKey = @ForeignKey(name = "user"))
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostComment that = (PostComment) o;
        return getCommentId().equals(that.getCommentId()) && Objects.equals(getParentCommentId(), that.getParentCommentId())
                && getPostCommentId().equals(that.getPostCommentId()) && getUserCommentId().equals(that.getUserCommentId())
                && getCommentTime().equals(that.getCommentTime()) && getCommentText().equals(that.getCommentText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommentId(), getParentCommentId(), getPostCommentId(), getUserCommentId(), getCommentTime(), getCommentText());
    }
}

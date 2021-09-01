package ru.pnapreenko.blogengine.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "posts_votes")
@ToString
@Getter
@Setter
@RequiredArgsConstructor
public class PostVote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer postVoteId;

    @Column(name = "user_id", nullable = false, insertable = false, updatable = false)
    private Integer userVotedId;

    @Column(name = "post_id", nullable = false, insertable = false, updatable = false)
    private Integer postVotedId;

    @Column(name = "time", nullable = false)
    private Date postVoteTime;

    @Column(name = "value", nullable = false)
    private Short voteValue;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "user"))
    private User user;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "post_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "post"))
    private Post post;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostVote postVote = (PostVote) o;
        return getPostVoteId().equals(postVote.getPostVoteId()) && getUserVotedId().equals(postVote.getUserVotedId())
                && getPostVotedId().equals(postVote.getPostVotedId()) && getPostVoteTime().equals(postVote.getPostVoteTime())
                && getVoteValue().equals(postVote.getVoteValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPostVoteId(), getUserVotedId(), getPostVotedId(), getPostVoteTime(), getVoteValue());
    }
}

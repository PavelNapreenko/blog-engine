package ru.pnapreenko.blogengine.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tag2post")
@ToString
@Getter
@Setter
@RequiredArgsConstructor
public class TagToPost {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer tagAndPostRelationId;

    @Column(name = "post_id", nullable = false, insertable = false, updatable = false)
    private Integer postId;

    @Column(name = "tag_id", nullable = false, insertable = false, updatable = false)
    private Integer tagId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagToPost tagToPost = (TagToPost) o;
        return getTagAndPostRelationId().equals(tagToPost.getTagAndPostRelationId()) && getPostId().equals(tagToPost.getPostId())
                && getTagId().equals(tagToPost.getTagId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTagAndPostRelationId(), getPostId(), getTagId());
    }
}

package ru.pnapreenko.blogengine.model;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tag")
@ToString
@Getter
@Setter
@RequiredArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer tagId;

    @Column(name = "name", nullable = false)
    private String tagName;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "tag2post",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    private List<Post> posts;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return getTagId().equals(tag.getTagId()) && getTagName().equals(tag.getTagName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTagId(), getTagName());
    }
}

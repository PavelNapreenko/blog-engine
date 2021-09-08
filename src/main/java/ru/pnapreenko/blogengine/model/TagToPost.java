package ru.pnapreenko.blogengine.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tag2post")
@Data
@NoArgsConstructor(force = true)
public class TagToPost extends AbstractEntity{

    @NotNull
    @Column(name = "post_id", nullable = false, insertable = false, updatable = false)
    private Integer postId;

    @NotNull
    @Column(name = "tag_id", nullable = false, insertable = false, updatable = false)
    private Integer tagId;
}

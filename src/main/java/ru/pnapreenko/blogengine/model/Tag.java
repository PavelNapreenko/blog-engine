package ru.pnapreenko.blogengine.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "tags", indexes = {
        @Index(name = "idx_tags_name", columnList="name", unique = true),
})
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Tag extends AbstractEntity {

    @NotBlank
    @Column(nullable = false)
    private String name;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "tag2post",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    private Set<Post> posts;
}

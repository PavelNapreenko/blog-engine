package ru.pnapreenko.blogengine.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@Data
@NoArgsConstructor(force = true)
@EqualsAndHashCode(callSuper = true, of = {"name"})
@ToString(callSuper = true, of = {"name"})
public class Tag extends AbstractEntity {

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false)
    private String name;

    @NotNull
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "tags")
    @LazyCollection(LazyCollectionOption.EXTRA)
    private final Set<Post> posts = new HashSet<>();

    public Tag(String name) {
        this.name = name;
    }
}

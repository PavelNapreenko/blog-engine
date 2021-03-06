package ru.pnapreenko.blogengine.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Cache;
import ru.pnapreenko.blogengine.enums.ModerationStatus;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "posts")
@Data
@NoArgsConstructor(force = true)
@EqualsAndHashCode(callSuper = true, of = {"title", "time"})
@ToString(callSuper = true, of = {"title"})
public class Post extends AbstractEntity {

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "moderation_status", length = 10, nullable = false)
    private ModerationStatus moderationStatus = ModerationStatus.NEW;

    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "moderator_id")
    private User moderatedBy;

    @NotNull
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "user_id")
    private User author;

    @NotNull
    @Column(nullable = false)
    private Instant time;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @NotNull
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "tag2post",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<Tag> tags = new HashSet<>();

    @NotNull
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private final Set<PostVote> votes = new HashSet<>();

    @NotNull
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private final Set<PostComment> comments = new HashSet<>();

    public void addTag(@NotNull Tag tag) {
        tags.add(tag);
    }

    public void updateViewCount() {
        this.viewCount++;
    }
}

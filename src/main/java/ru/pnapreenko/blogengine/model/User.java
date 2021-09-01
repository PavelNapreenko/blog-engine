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
@Table(name = "users")
@ToString
@Getter
@Setter
@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "is_moderator", nullable = false)
    private Short isModerator;

    @Column(name = "reg_time", nullable = false)
    private Date regTime;

    @Basic(fetch = FetchType.LAZY)
    @ToString.Exclude
    @Column(name = "name", nullable = false)
    private String userName;

    @Column(name = "email", nullable = false)
    private String userEmail;

    @Column(name = "password", nullable = false)
    private String userPassword;

    @Column(name = "code")
    private String passwordRecoveryCode;

    @Column(name = "photo")
    private String userPhoto;

    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostVote> postVoteList;

    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostComment> postComments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getId().equals(user.getId()) && getIsModerator().equals(user.getIsModerator()) && getRegTime().equals(user.getRegTime())
                && getUserName().equals(user.getUserName()) && getUserEmail().equals(user.getUserEmail())
                && getUserPassword().equals(user.getUserPassword()) && Objects.equals(getPasswordRecoveryCode(), user.getPasswordRecoveryCode())
                && Objects.equals(getUserPhoto(), user.getUserPhoto());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getIsModerator(), getRegTime(), getUserName(), getUserEmail(), getUserPassword(), getPasswordRecoveryCode(),
                getUserPhoto());
    }
}

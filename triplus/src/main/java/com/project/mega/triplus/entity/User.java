package com.project.mega.triplus.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname")
    private String nickName;

    private String email;

    private String password;

    private String tel;

    private LocalDateTime joinedAt;

    private boolean emailVerified;

    private String emailCheckToken;

    private boolean telVerified;

    private Role role;

    @OneToMany
    private List<Place> placeLikes;

    @OneToMany(mappedBy = "user")
    private List<Plan> planLikes;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;
}

package com.project.mega.triplus.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname")
    private String nickName;

    private String email;

    private String password;

    private String principal;

    private String tel;

    private LocalDateTime joinedAt;

    private boolean emailVerified;

    private String emailCheckToken;

    private boolean telVerified;

    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @OneToMany
    private List<Place> placeLikes;

    @OneToMany
    private List<Plan> planLikes;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Plan> myPlans = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Review> reviews = new ArrayList<>();

    @Builder
    public User(String nickName, String password, String principal,String email, SocialType socialType, LocalDateTime joinedAt){
        this.nickName=nickName;
        this.password=password;
        this.email=email;
        this.principal=principal;
        this.socialType=socialType;
        this.joinedAt=joinedAt;
    }
}

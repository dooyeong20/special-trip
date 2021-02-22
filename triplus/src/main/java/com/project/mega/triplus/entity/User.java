package com.project.mega.triplus.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity

@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"email", "nickname"})})
@Getter @Setter @EqualsAndHashCode(of="id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nickName;

    @NotNull
    private String email;

    @NotNull
    private String password;

    private LocalDateTime joinedAt;

    private boolean emailVerified;

    private String emailCheckToken;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany
    private List<Place> placeLikes;

    @OneToMany
    private List<Plan> planLikes;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Plan> myPlans = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Review> reviews = new ArrayList<>();

    public void generateEmailCheckToken(){
        emailCheckToken = UUID.randomUUID().toString();
    }

    public boolean isValidToken(String token){ return token.equals(emailCheckToken);
    }

    public void completeJoin(){
        setEmailVerified(true);
        setJoinedAt(LocalDateTime.now());
    }
}

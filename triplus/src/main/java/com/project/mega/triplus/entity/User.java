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

    private String nickName;

    @NotNull
    private String email;


    private String password;

    private LocalDateTime joinedAt;

    private boolean emailVerified;

    private String emailCheckToken;

    private boolean telVerified;


    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany
    private List<Place> placeLikes;

    @OneToMany
    private List<Plan> planLikes;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @Builder.Default
    private List<Plan> myPlans = new ArrayList<>();

    @Builder.Default
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

    @Builder
    public User(String nickName, String email, Role role){
        this.nickName=nickName;
        this.email=email;
        this.role=role;
    }

    public User update(String nickName, String email){
        this.nickName=nickName;
        this.email=email;

        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }

}

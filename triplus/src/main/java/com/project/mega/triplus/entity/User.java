package com.project.mega.triplus.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter @Setter
@Builder @EqualsAndHashCode(of="id")
@AllArgsConstructor @NoArgsConstructor
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

    public void generateEmailCheckToken() {

        emailCheckToken = UUID.randomUUID().toString();
    }

//    public boolean isValidToken(String token){
//        return token.equals(emailCheckToken);
//    }
//
//    public void completeJoin(){
//        setEmailVerified(true);
//        setJoinedAt(LocalDateTime.now());
//    }
}

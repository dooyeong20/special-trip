package com.project.mega.triplus.oauth2;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@Getter
public class Oauth2User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Oauth2Role role;

    @Builder
    public Oauth2User(String name, String email, Oauth2Role role){
        this.name=name;
        this.email=email;
        this.role=role;
    }

    public Oauth2User update(String name){
        this.name=name;

        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }

}

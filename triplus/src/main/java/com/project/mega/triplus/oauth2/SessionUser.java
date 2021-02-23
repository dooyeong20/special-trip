package com.project.mega.triplus.oauth2;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;


    public SessionUser(Oauth2User oauth2User) {
        this.name = oauth2User.getName();
        this.email = oauth2User.getEmail();

    }
}

package com.project.mega.triplus.oauth2;

import com.project.mega.triplus.entity.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String nickName;
    private String email;


    public SessionUser(User user) {
        this.nickName = user.getNickName();
        this.email = user.getEmail();

    }
}

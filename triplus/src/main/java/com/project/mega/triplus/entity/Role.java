package com.project.mega.triplus.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, ADMIN, GUEST;

    @Override
    public String getAuthority() {
        return this.name();
    }
}

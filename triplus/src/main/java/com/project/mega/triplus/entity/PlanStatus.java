package com.project.mega.triplus.entity;

import org.springframework.security.core.GrantedAuthority;

public enum PlanStatus implements GrantedAuthority {
    WRITING, COMPLETE;

    @Override
    public String getAuthority() {
        return this.name();
    }
}

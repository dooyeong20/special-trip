package com.project.mega.triplus.repository;

import com.project.mega.triplus.entity.Oauth2User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Oauth2UserRepository extends JpaRepository<Oauth2User, Long> {
    Oauth2User findByEmail(String email);
}

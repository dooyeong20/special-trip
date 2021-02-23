package com.project.mega.triplus.oauth2;

import com.project.mega.triplus.oauth2.Oauth2User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Oauth2UserRepository extends JpaRepository<Oauth2User, Long> {
    Optional<Oauth2User> findByEmail(String email);
}

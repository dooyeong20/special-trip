package com.project.mega.triplus;

import com.project.mega.triplus.entity.User;
import com.project.mega.triplus.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("로그인 확인 - 비밀번호 틀림")
    @Test
    void loginSubmit_with_wrong_password() throws Exception{
        // Given
        User user = User.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("1234"))
                .build();

        User newUser = userRepository.save(user);
        newUser.generateEmailCheckToken();

        // when
        mockMvc.perform(post("/login")
                .param("username", "test@test.com")
                .param("password", "wrong_password")
                .with(csrf()))

                // then
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }

    @DisplayName("로그인 확인 - 이메일 비번 모두 맞음")
    @Test
    void loginSubmit_with_correct_email_and_password() throws Exception{
        // Given
        User user = User.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("1234"))
                .build();

        User newUser = userRepository.save(user);
        newUser.generateEmailCheckToken();

        // when
        mockMvc.perform(post("/login")
                .param("username", "test@test.com")
                .param("password", "1234")
                .with(csrf()))

                // then
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated());
    }

}

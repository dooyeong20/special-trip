package com.project.mega.triplus.service;

import com.project.mega.triplus.config.AppProperties;
import com.project.mega.triplus.entity.Role;
import com.project.mega.triplus.entity.User;
import com.project.mega.triplus.form.JoinForm;
import com.project.mega.triplus.form.JoinFormValidator;
import com.project.mega.triplus.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.project.mega.triplus.util.EmailMessage;
import com.project.mega.triplus.util.EmailService;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.annotation.PostConstruct;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final JoinFormValidator joinFormValidator;

    private final PasswordEncoder passwordEncoder;

    private final AppProperties appProperties;

    private final TemplateEngine templateEngine;

    private final EmailService emailService;

    @InitBinder("signupForm")
    public void initBinder(WebDataBinder webDataBinder){ webDataBinder.addValidators(joinFormValidator);}

    public User saveNewUser(JoinForm joinForm){
        User user = User.builder()
                .email(joinForm.getEmail())
                .password(passwordEncoder.encode(joinForm.getPassword()))
                .nickName(joinForm.getNickname())
                .build();

        return userRepository.save(user);
    }

    public void sendJoinConfirmEmail(User newUser){
        sendEmail(newUser, "Triplus - 회원 가입 인증", "/check-email-token");
    }

    public void login(User user){
        UserUser userUser = new UserUser(user);
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        userUser,
                        userUser.getUser().getPassword(),
                        userUser.getAuthorities()
                );

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
    }

    public User processNewUser(JoinForm joinForm){
        User newUser = saveNewUser(joinForm);
        newUser.generateEmailCheckToken();
        newUser.setRole(Role.USER);
        sendJoinConfirmEmail(newUser);

        return newUser;
    }

    private void sendEmail(User user, String subject, String url) {
        Context context = new Context();
        context.setVariable("link", url + "?token=" + user.getEmailCheckToken() + "&email=" + user.getEmail());
        context.setVariable("host", appProperties.getHost());
        context.setVariable("linkName", "이메일 인증하기");
        context.setVariable("message", "서비스 이용을 위해 링크를 클릭해주세요.");

        String html = templateEngine.process("mail/simple-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(user.getEmail())
                .subject(subject)
                .message(html)
                .build();
        emailService.sendEmail(emailMessage);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().getAuthority())
                .build();
    }


    public void sendMailResetPassword(String email) {
        User user = userRepository.findByEmail(email);

        if(user == null) {
            return;
        }
    }
}

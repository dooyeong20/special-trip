package com.project.mega.triplus.service;

import com.project.mega.triplus.config.AppProperties;
import com.project.mega.triplus.entity.Role;
import com.project.mega.triplus.entity.User;
import com.project.mega.triplus.form.SignupForm;
import com.project.mega.triplus.form.SignupFormValidator;
import com.project.mega.triplus.repository.UserRepository;
import com.project.mega.triplus.util.EmailMessage;
import com.project.mega.triplus.util.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.PostConstruct;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final SignupFormValidator signupFormValidator;

    private final PasswordEncoder passwordEncoder;

    private final AppProperties appProperties;

    private final TemplateEngine templateEngine;

    private final EmailService emailService;

    @InitBinder("signupForm")
    public void initBinder(WebDataBinder webDataBinder){ webDataBinder.addValidators(signupFormValidator);}

    public User saveNewUser(SignupForm signupForm){
        User user = User.builder()
                .email(signupForm.getEmail())
                .password(passwordEncoder.encode(signupForm.getPassword()))
                .nickName(signupForm.getNickname())
                .build();
        User newUser = userRepository.save(user);
        return newUser;
    }

    public void sendSignupConfirmEmail(User newUser){
        sendEmail(newUser, "Triplus - 회원 가입 인증", "/check-email-token");
    }

    public User processNewNumber(SignupForm signupForm){
        User newUser = saveNewUser(signupForm);
        newUser.generateEmailCheckToken();
        newUser.setRole(Role.USER);
        sendSignupConfirmEmail(newUser);
        return newUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if(user == null){
            throw new UsernameNotFoundException(username);
        }
        return new UserUser(user);
    }

    private void sendEmail(User user, String subject, String url){
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
}

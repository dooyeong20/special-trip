package com.project.mega.triplus.form;

import com.project.mega.triplus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SignupFormValidator implements Validator {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignupForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignupForm signupForm = (SignupForm) target;

        // 이메일 중복 확인인
        if(userRepository.existsByEmail(signupForm.getEmail())){
            errors.rejectValue(
                    "email",
                    "invalid.email",
                    new Object[]{signupForm.getEmail()},
                    "이미 사용 중인 이메일입니다."
            );
        }
    }
}

package com.project.mega.triplus.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class JoinForm {

    @NotBlank
    private String nickname;

    @NotBlank
    @Email
    @Length(min = 5, max = 40)
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String agreeTermsOfService;
}

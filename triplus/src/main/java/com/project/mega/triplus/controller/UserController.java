package com.project.mega.triplus.controller;

import com.project.mega.triplus.entity.User;
import com.project.mega.triplus.form.JoinForm;
import com.project.mega.triplus.repository.UserRepository;
import com.project.mega.triplus.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.Valid;

@Controller
public class UserController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Autowired
    UserRepository userRepository;

    @Transactional
    public String joinSubmit(@Valid JoinForm joinForm, Errors errors){
        if (errors.hasErrors()){
            logger.info("!!!!!!!!!!!!!!!!!!!!!");
            return "fragment/header";
        }

        User newUser = userService.processNewNumber(joinForm);
        userService.login(newUser);

        return "redirect:/";
    }

    @GetMapping("/check-email-token")
    @Transactional
    public String checkEmailToken(String token, String email, Model model){
        User user = userRepository.findByEmail(email);

        if(user == null){
            model.addAttribute("error", "wrong.email");
            return "user/checked-email";
        }

        if(!user.isValidToken(token)){
            model.addAttribute("error", "wrong.token");
            return "user/checked-email";
        }
        user.completeJoin();
        userService.login(user);
        model.addAttribute("email", user.getEmail());
        return "user/checked-email";
    }


}

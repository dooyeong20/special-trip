package com.project.mega.triplus.controller;


import com.project.mega.triplus.entity.User;
import com.project.mega.triplus.form.JoinForm;
import com.project.mega.triplus.repository.UserRepository;
import com.project.mega.triplus.service.CurrentUser;
import com.project.mega.triplus.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final MainController mainController;

    private final UserService userService;

    private final UserRepository userRepository;


    @Transactional
    @PostMapping("/join")
    public String joinSubmit(
            @RequestParam(value = "nickname") String nickname,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "checklist") String checklist
        ){
        JoinForm joinForm = new JoinForm();

        joinForm.setNickname(nickname);
        joinForm.setEmail(email);
        joinForm.setPassword(password);
        joinForm.setAgreeTermsOfService(checklist);

        User newUser = userService.processNewUser(joinForm);
        userService.login(newUser);

        return "redirect:/";
    }

//    @PostMapping("/checkNickName")
//    @ResponseBody
//    public String checkNickName(@RequestParam(value = "nickNameCheck")String nickName){
//        String result=null;
//
//        if(userRepository.existsByNickName(nickName)){
//            result="nickNameNO";
//        } else{
//            result="nickNameOK";
//        }
//
//        return result;
//    }
//
//    @Transactional
//    @PostMapping("/join")
//    @ResponseBody
//    public String joinSubmit(
//            @RequestParam(value = "nickname") String nickname,
//            @RequestParam(value = "email") String email,
//            @RequestParam(value = "password") String password,
//            @RequestParam(value = "checklist") String checklist
//        ){
//        JoinForm joinForm = new JoinForm();
//
//        joinForm.setNickname(nickname);
//        joinForm.setEmail(email);
//        joinForm.setPassword(password);
//        joinForm.setAgreeTermsOfService(checklist);
//
//        User newUser = userService.processNewUser(joinForm);
//        userService.login(newUser);
//
//        return "joinSuccess";
//    }

    @GetMapping("/check-email-token")
    @Transactional
    public String checkEmailToken(String token, String email, Model model){
        User user = userRepository.findByEmail(email);

        if(user == null){
            model.addAttribute("error", "wrong.email");
            return mainController.index(model);
        }

        if(!user.isValidToken(token)){
            model.addAttribute("error", "wrong.token");
            return mainController.index(model);
        }

        user.completeJoin();
        userService.login(user);

        model.addAttribute("nickname", user.getNickName());
        model.addAttribute("checked", true);

        return mainController.index(model);
    }
}

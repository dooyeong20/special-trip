package com.project.mega.triplus.controller;

import com.project.mega.triplus.entity.Role;
import com.project.mega.triplus.entity.User;
import com.project.mega.triplus.repository.UserRepository;
import com.project.mega.triplus.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @GetMapping("/change-password")
    public String changePasswordForm(){
        return "view/user/change-password";
    }

    @PostMapping("/change-password")
    public String changePasswordSubmit(String email, Model model){

        // 메일 보내기
        userService.sendMailResetPassword(email);

        // 결과 view 에
        model.addAttribute("email", email);
        model.addAttribute("result_code", "password.reset.send");

        // view/notify 로 이동
        return "view/notify";
    }

}

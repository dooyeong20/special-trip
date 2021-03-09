package com.project.mega.triplus.controller;


import com.project.mega.triplus.entity.User;
import com.project.mega.triplus.repository.UserRepository;
import com.project.mega.triplus.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final MainController mainController;

    private final UserService userService;

    private final UserRepository userRepository;

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

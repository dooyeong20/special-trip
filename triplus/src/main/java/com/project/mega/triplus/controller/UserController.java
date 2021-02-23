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
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

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

    @Autowired
    UserRepository userRepository;

//    // 닉네임 중복 체크 컨트롤러
//    @RequestMapping(value = "/user/nicknameCheck", method = RequestMethod.GET)
//    @ResponseBody
//    public int nicknameCheck(@RequestParam("nickname") String nickname) {
//        return userService.nicknameCheck(nickname);
//    }


    @Transactional
    @PostMapping("/join")
    public String joinSubmit(Model model,
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

    @GetMapping("/check-email-token")
    @Transactional
    public String checkEmailToken(String token, String email, Model model){
        User user = userRepository.findByEmail(email);

        if(user == null){
            model.addAttribute("error", "wrong.email");
            return "index";
        }

        if(!user.isValidToken(token)){
            model.addAttribute("error", "wrong.token");
            return "index";
        }

        user.completeJoin();
        userService.login(user);

        model.addAttribute("nickname", user.getNickName());
        model.addAttribute("checked", true);

        return "index";
    }

}

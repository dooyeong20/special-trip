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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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


    @GetMapping("/password-issue")
    public String passwordIssueForm(){
        return "/view/password-issue";
    }

    @PostMapping("/password-issue")
    @ResponseBody
    public String changePasswordSubmit(@RequestParam(value = "findPwEmail") String email, Model model){
        String result=null;

        try {
            // 메일 보내기
            userService.sendMailResetPassword(email);

            // 결과 view에
            model.addAttribute("email", email);
            model.addAttribute("result_code", "password.reset.send");
            result = "이메일 전송 완료";
        } catch (Exception e){
            result = "오류";
        }
        return result;
    }

    @GetMapping("/reset-password")
    public String resetPasswordForm(String token, String email, Model model){
        // email이 유효한지 확인
        User user = userRepository.findByEmail(email);
        if(user == null){
            model.addAttribute("result", false);
            return "/user/reset-password";
        }

        // 그 emailCheckToken과 token을 비교
        String emailCheckToken = user.getEmailCheckToken();

        // 틀리면 에러
        if (! emailCheckToken.equals(token)){
            model.addAttribute("result", false);
            return "/user/reset-password";
        }
        // 맞으면 비밀번호 재설정 페이지로
        model.addAttribute("email", email);
        model.addAttribute("result", true);
        return "/user/reset-password";
    }

    @PostMapping("/reset-password")
    @ResponseBody
    public String resetPasswordSubmit(@RequestParam(value = "email")String email, @RequestParam(value = "newPassword") String password){
        userService.processResetPassword(email, password);

        User user = userRepository.findByEmail(email);

        userService.login(user);

        return "resetSuccess";
    }
}

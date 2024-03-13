package com.cos.photogramstart.web;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.service.AuthService;
import com.cos.photogramstart.web.dto.auth.SignupDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@RequiredArgsConstructor // final 필드를 DI 할떄 사용
@Controller // 1.IOC 2.파일을 리턴하는 컨트롤러
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @GetMapping("/auth/signin")
    public String signinForm(){
        return "auth/signin";
    }

    @GetMapping("/auth/signup")
    public String signupForm(){
        return "auth/signup";
    }

    @PostMapping("/auth/signup")
    public String signup(@Valid SignupDto signupDto, BindingResult bindingResult){ // key=value (x-www-form-urlencoded)

//        if(bindingResult.hasErrors()){
//            Map<String, String> errorMap = new HashMap<>();
//
//            for(FieldError error : bindingResult.getFieldErrors()){
//                errorMap.put(error.getField(), error.getDefaultMessage());
//                System.out.println("=======================");
//                System.out.println(error.getDefaultMessage());
//                System.out.println("=======================");
//            }
//            throw new CustomValidationException("유효성 검사 실패함",errorMap);
//        }else {
//            log.info(signupDto.toString());
//            User user = signupDto.toEntity();
//            log.info(user.toString());
//            User userEntity = authService.회원가입(user);
//            System.out.println(userEntity);
//            return "auth/signin";
//        }
        User user = signupDto.toEntity();
        authService.회원가입(user);
        return "/auth/signin";
    }

}

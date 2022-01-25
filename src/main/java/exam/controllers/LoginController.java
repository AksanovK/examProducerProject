package exam.controllers;

import exam.models.dto.EmailPasswordDto;
import exam.models.dto.TokenDto;
import exam.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody EmailPasswordDto emailPassword) throws Throwable {
        System.out.println("AAAAAAAAAAaa");
        return ResponseEntity.ok(loginService.login(emailPassword));
    }

}

package com.gest.art.security.auth;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@CrossOrigin("*")
public class AuthResponseController {

    private final AuthenticationService service;

    @GetMapping("/verify-email")
    public String verifyE(@RequestParam("token") String token, Model model) {
        String result = service.validateVerificationToken(token);
        // final var modelAndView = new ModelAndView();
        model.addAttribute("message", "BIENVENUE SUR NOTRE PLATEFOR!");
        System.out.println("res" + result);
        if (result.equals("valid")) {
            // modelAndView.setViewName("index.html");
            return "index";
        } else {
            String log = "Invalide verification token.";
            return log;
        }

    }
}

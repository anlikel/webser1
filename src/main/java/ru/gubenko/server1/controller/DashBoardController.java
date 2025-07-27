package ru.gubenko.server1.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.gubenko.server1.service.UserService;

@Controller
public class DashBoardController {
    private final UserService userService;

    public DashBoardController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("username", authentication.getName());
        }
        return "dashboard";
    }

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model){
        if (authentication != null && authentication.isAuthenticated()) {
            var user=userService.findByUserName(authentication.getName());
            model.addAttribute("user",user);
        }
        return "profile";
    }

    @GetMapping("/settings")
    public String settings(Authentication authentication, Model model){
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("username", authentication.getName());
        }
        return "settings";
    }

}

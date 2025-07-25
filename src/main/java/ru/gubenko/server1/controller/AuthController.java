package ru.gubenko.server1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.gubenko.server1.model.entity.User;
import ru.gubenko.server1.service.UserService;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        model.addAttribute("user",new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
        @RequestParam String username,
        @RequestParam String password,
        @RequestParam String confirmPassword,
        Model model){

        if(confirmPassword.length()<4 || password.length()<4){
            model.addAttribute("error","password short");
            return "register";
        }

        if(!password.equals(confirmPassword)){
            model.addAttribute("error","password mismatch");
            return "register";
        }
        try{
            userService.registerNewUser(username,password);
            model.addAttribute("success","successfull registration");
            return "login";
        }
        catch(Exception e){
            model.addAttribute("error",e.getMessage());
            User user=new User();
            user.setUsername(username);
            user.setPassword("");
            model.addAttribute("user",user);
            return "register";
        }
    }

    }


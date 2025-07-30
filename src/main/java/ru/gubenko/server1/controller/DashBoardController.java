package ru.gubenko.server1.controller;

import jakarta.validation.constraints.Positive;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.gubenko.server1.service.UserService;

import java.util.Arrays;
import java.util.List;

@Controller
public class DashBoardController {
    private final UserService userService;

    public DashBoardController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            model.addAttribute("username", authentication.getName());
            model.addAttribute("unreadCount",userService.getUnreadCount(username));
            model.addAttribute("messages",userService.getUserMessages(username));
        }
        return "dashboard";
    }

    public String markMessagesAsRead(Authentication authentication){
        if(authentication!=null && authentication.isAuthenticated()){
            userService.markAllAsRead(authentication.getName());
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model){
        if (authentication != null && authentication.isAuthenticated()) {
            var user=userService.findByUserName(authentication.getName());
            model.addAttribute("user",user);
            model.addAttribute("pageTtile","профиль");
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

    @PostMapping("/profile/update")
    public String updateProfile(
        @RequestParam String email,
        @RequestParam String phone,
        Authentication authentication,
        RedirectAttributes redirectAttributes){
        if(authentication!=null && authentication.isAuthenticated()){
            try{
                userService.updateUserContactInfo(authentication.getName(), email,phone);
                redirectAttributes.addFlashAttribute("success","обновлено");
            }
            catch(Exception e){
                redirectAttributes.addFlashAttribute("errorMessage","ошибка при обновлении данных "
                +e.getMessage());
            }
        }
        return "redirect:/profile";
    }

}

package ru.gubenko.server1.controller;

import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.gubenko.server1.model.entity.Message;
import ru.gubenko.server1.service.MessageService;
import ru.gubenko.server1.service.UserService;

@Controller
public class DashBoardController {
    private final UserService userService;
    private final MessageService messageService;

    public DashBoardController(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService=messageService;
    }

    @GetMapping("/dashboard")
    public String dashboard(
            Authentication authentication,
            Model model,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size,
            RedirectAttributes redirectAttributes) {

        if (authentication != null && authentication.isAuthenticated()) {
            try {
                String username = authentication.getName();
                Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

                Page<Message> messagesPage = messageService.getUserMessages(username, pageable);

                model.addAttribute("username", username);
                model.addAttribute("unreadCount", messageService.getUnreadCount(username));
                model.addAttribute("messages", messagesPage.getContent());
                model.addAttribute("currentPage", messagesPage.getNumber());
                model.addAttribute("totalPages", messagesPage.getTotalPages());
                model.addAttribute("size", size);
            }
            catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Ошибка при загрузке сообщений");
                return "redirect:/dashboard";
            }
        }
        return "dashboard";
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
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String phone,
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

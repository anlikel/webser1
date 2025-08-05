package ru.gubenko.server1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.gubenko.server1.model.entity.Message;
import ru.gubenko.server1.model.entity.User;
import ru.gubenko.server1.repository.UserRepository;
import ru.gubenko.server1.service.MessageService;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;
    private final UserRepository userRepository;

    @Autowired
    public MessageController(MessageService messageService,UserRepository userRepository){
        this.messageService=messageService;
        this.userRepository=userRepository;
    }

    @GetMapping("/new")
    public String newMessage(Model model){
        model.addAttribute("message",new Message());
        model.addAttribute("users",userRepository.findAll());
        model.addAttribute("currentTime", LocalDateTime.now());
        return "message/new";
    }

    @PostMapping("/send")
    public String sendMessage(

            @RequestParam
            String recipientUserName,
            @RequestParam(required=false)
            String content,
            Authentication authentication,
            RedirectAttributes redirectAttributes
            ){

        try {
            String senderUserName = authentication.getName();
            messageService.sendMessage(senderUserName, recipientUserName, content);
            redirectAttributes.addFlashAttribute("success","message send");
            }
            catch(Exception e){
            redirectAttributes.addFlashAttribute("fail","message not send "+ e.getMessage());
            }
        return "redirect:/messages";
    }

    @GetMapping
    public String viewMessages(
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size,
            Model model,
            Authentication authentication){

        String username= authentication.getName();
        Pageable pageable= PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<Message> messagePage=messageService.getUserMessages(username,pageable);

        model.addAttribute("messages",messagePage.getContent());
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages",messagePage.getTotalPages());
        model.addAttribute("totalItems",messagePage.getTotalElements());

        return "message/list";
    }

    @GetMapping("/{id}")
    public String viewMessage(
            @PathVariable Long id,
            Model model,
            @AuthenticationPrincipal User currentUser) {
        System.out.println("Current user in controller: " + currentUser);
        Message message=messageService.getMessage(id,currentUser);
        model.addAttribute("message",message);
        return "message/view";
    }

    @PostMapping("/mark-as-read")
    public String markAllAsRead(Authentication authentication,RedirectAttributes redirectAttributes){
        if(authentication!=null && authentication.isAuthenticated()){
            try{
                messageService.markAllAsRead(authentication.getName());
                redirectAttributes.addFlashAttribute("success","all messages marked as read");
            }
            catch(Exception e){
                redirectAttributes.addFlashAttribute("error","cant renew message status");
            }
        }
        return "redirect:/messages";
    }
}

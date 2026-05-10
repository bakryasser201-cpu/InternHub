package com.internhub.controller;

import com.internhub.dto.LoginRequestDTO;
import com.internhub.dto.RegisterRequestDTO;
import com.internhub.model.User;
import com.internhub.model.UserRole;
import com.internhub.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequestDTO());
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginRequest") LoginRequestDTO dto, BindingResult result, HttpSession session, Model model) {
        if (result.hasErrors()) {
            return "login";
        }
        try {
            User user = authService.login(dto);
            session.setAttribute("userId", user.getId());
            session.setAttribute("role", user.getRole());
            if (user.getRole() == UserRole.STUDENT) {
                session.setAttribute("studentId", authService.getStudentProfile(user.getId()).getId());
                return "redirect:/student/dashboard";
            }
            session.setAttribute("companyId", authService.getCompanyProfile(user.getId()).getId());
            return "redirect:/company/dashboard";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("loginError", ex.getMessage());
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequestDTO());
        model.addAttribute("roles", UserRole.values());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerRequest") RegisterRequestDTO dto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", UserRole.values());
            return "register";
        }
        try {
            authService.register(dto);
            return "redirect:/login?registered";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("registerError", ex.getMessage());
            model.addAttribute("roles", UserRole.values());
            return "register";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}

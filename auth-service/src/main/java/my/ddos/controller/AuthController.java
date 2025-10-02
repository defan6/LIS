package my.ddos.controller;

import lombok.RequiredArgsConstructor;
import my.ddos.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    
    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           Model model) {
        if (error != null) {
            model.addAttribute("error", "Неверное имя пользователя или пароль");
        }
        if (logout != null) {
            model.addAttribute("message", "Вы успешно вышли из системы");
        }
        return "auth/login";
    }
    
    @GetMapping("/register")
    public String registerForm() {
        return "auth/register";
    }
    
    @PostMapping("/register")
    public String register(@RequestParam String username,
                          @RequestParam String email,
                          @RequestParam String password,
                          @RequestParam String confirmPassword,
                          @RequestParam(required = false) String firstName,
                          @RequestParam(required = false) String lastName,
                          RedirectAttributes redirectAttributes) {
        
        try {
            // Валидация
            if (!password.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Пароли не совпадают");
                return "redirect:/api/v1/auth/register";
            }
            
            if (password.length() < 6) {
                redirectAttributes.addFlashAttribute("error", "Пароль должен содержать минимум 6 символов");
                return "redirect:/api/v1/auth/register";
            }
            
            if (userService.existsByUsername(username)) {
                redirectAttributes.addFlashAttribute("error", "Пользователь с таким именем уже существует");
                return "redirect:/api/v1/auth/register";
            }
            
            if (userService.existsByEmail(email)) {
                redirectAttributes.addFlashAttribute("error", "Пользователь с таким email уже существует");
                return "redirect:/api/v1/auth/register";
            }
            
            // Регистрация пользователя
            userService.registerUser(username, email, password, firstName, lastName);
            
            redirectAttributes.addFlashAttribute("message", "Регистрация успешна! Теперь вы можете войти в систему");
            return "redirect:/api/v1/auth/login";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при регистрации: " + e.getMessage());
            return "redirect:/api/v1/auth/register";
        }
    }
    
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
}

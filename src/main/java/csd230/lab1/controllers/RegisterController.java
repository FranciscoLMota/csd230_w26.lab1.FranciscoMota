package csd230.lab1.controllers;
import csd230.lab1.entities.UserEntity;
import csd230.lab1.repositories.UserEntityRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
@Tag(name = "Registration", description = "Operations related to User Registration")
@Controller
public class RegisterController {
    @Autowired
    private UserEntityRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Operation(summary = "Show the registration form", description = "Returns the HTML form used to register a user")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the registration form")
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserEntity());
        return "register";
    }
    @Operation(summary = "Register a user", description = "Creates a user account and redirects to the login page")
    @ApiResponse(responseCode = "302", description = "User registered; redirecting to the login page")
    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserEntity user) {
        // 1. Encode the password! Do not save plain text.
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 2. Set default role
        user.setRole("USER");

        // 3. Save to DB
        userRepository.save(user);
        // 4. Redirect to login
        return "redirect:/login";
    }
}

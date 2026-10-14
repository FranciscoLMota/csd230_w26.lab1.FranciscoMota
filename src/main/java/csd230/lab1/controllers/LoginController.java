package csd230.lab1.controllers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Tag(name = "Authentication", description = "Operations related to User Authentication")
@Controller
public class LoginController {
    @Operation(summary = "Show the login page", description = "Returns the HTML login view")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the login page")
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}

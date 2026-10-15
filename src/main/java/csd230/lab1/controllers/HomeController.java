package csd230.lab1.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Catalog", description = "Operations related to Product Catalog Navigation")
@Controller
public class HomeController {

    @Operation(summary = "Show the product catalog", description = "Returns the product type selection page")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the product catalog")
    @GetMapping("/")
    public String index() {
        return "index";
    }
}

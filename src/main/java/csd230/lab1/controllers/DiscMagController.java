package csd230.lab1.controllers;

import csd230.lab1.entities.CartEntity;
import csd230.lab1.entities.DiscMagEntity;
import csd230.lab1.repositories.CartEntityRepository;
import csd230.lab1.repositories.DiscMagRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Disc Magazines", description = "Operations related to Disc Magazine Management")
@Controller
@RequestMapping("/disc-mags")
public class DiscMagController {
    private final DiscMagRepository discMagRepository;
    private final CartEntityRepository cartRepository;

    public DiscMagController(DiscMagRepository discMagRepository, CartEntityRepository cartRepository) {
        this.discMagRepository = discMagRepository;
        this.cartRepository = cartRepository;
    }

    @Operation(summary = "Get all disc magazines", description = "Returns the HTML view of the disc magazine list")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved disc magazine list")
    @GetMapping
    public String getAllDiscMags(Model model) {
        model.addAttribute("discMags", discMagRepository.findAll());
        return "discMag/discMagList";
    }

    @Operation(summary = "Get a disc magazine by ID", description = "Returns the HTML view containing the selected disc magazine's details")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved disc magazine details")
    @GetMapping("/{id}")
    public String getDiscMagById(@PathVariable Long id, Model model) {
        model.addAttribute("discMag", discMagRepository.findById(id).orElse(null));
        return "discMag/discMagDetails";
    }

    @Operation(summary = "Show the add disc magazine form", description = "Returns the HTML form used to create a disc magazine")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the add disc magazine form")
    @GetMapping("/add")
    public String addDiscMagForm(Model model) {
        model.addAttribute("discMag", new DiscMagEntity());
        return "discMag/addDiscMag";
    }

    @Operation(summary = "Add a disc magazine", description = "Creates a disc magazine and redirects to the disc magazine list")
    @ApiResponse(responseCode = "302", description = "Disc magazine created; redirecting to the disc magazine list")
    @PostMapping("/add")
    public String addDiscMag(@ModelAttribute DiscMagEntity discMag) {
        discMagRepository.save(discMag);
        return "redirect:/disc-mags";
    }

    @Operation(summary = "Show the edit disc magazine form", description = "Returns the HTML form used to edit the selected disc magazine")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the edit disc magazine form")
    @GetMapping("/edit/{id}")
    public String editDiscMagForm(@PathVariable Long id, Model model) {
        model.addAttribute("discMag", discMagRepository.findById(id).orElse(null));
        return "discMag/editDiscMag";
    }

    @Operation(summary = "Edit a disc magazine", description = "Updates the selected disc magazine and redirects to the disc magazine list")
    @ApiResponse(responseCode = "302", description = "Disc magazine updated; redirecting to the disc magazine list")
    @PostMapping("/edit/{id}")
    public String editDiscMag(@PathVariable Long id, @ModelAttribute DiscMagEntity updatedDiscMag) {
        discMagRepository.findById(id).ifPresent(discMag -> {
            discMag.setTitle(updatedDiscMag.getTitle());
            discMag.setPrice(updatedDiscMag.getPrice());
            discMag.setCopies(updatedDiscMag.getCopies());
            discMag.setOrderQty(updatedDiscMag.getOrderQty());
            discMag.setCurrentIssue(updatedDiscMag.getCurrentIssue());
            discMag.setHasDisc(updatedDiscMag.isHasDisc());
            discMagRepository.save(discMag);
        });
        return "redirect:/disc-mags";
    }

    @Operation(summary = "Delete a disc magazine", description = "Deletes the selected disc magazine and redirects to the disc magazine list")
    @ApiResponse(responseCode = "302", description = "Disc magazine deleted; redirecting to the disc magazine list")
    @GetMapping("/delete/{id}")
    public String deleteDiscMag(@PathVariable Long id) {
        discMagRepository.findById(id).ifPresent(discMag -> {
            for (CartEntity cart : discMag.getCarts()) {
                cart.getProducts().remove(discMag);
                cartRepository.save(cart);
            }
            discMagRepository.delete(discMag);
        });
        return "redirect:/disc-mags";
    }
}

package csd230.lab1.controllers;

import csd230.lab1.entities.CartEntity;
import csd230.lab1.entities.MagazineEntity;
import csd230.lab1.repositories.CartEntityRepository;
import csd230.lab1.repositories.MagazineRepository;
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

@Tag(name = "Magazines", description = "Operations related to Magazine Management")
@Controller
@RequestMapping("/magazines")
public class MagazineController {
    private final MagazineRepository magazineRepository;
    private final CartEntityRepository cartRepository;

    public MagazineController(MagazineRepository magazineRepository, CartEntityRepository cartRepository) {
        this.magazineRepository = magazineRepository;
        this.cartRepository = cartRepository;
    }

    @Operation(summary = "Get all magazines", description = "Returns the HTML view of the magazine list")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved magazine list")
    @GetMapping
    public String getAllMagazines(Model model) {
        model.addAttribute("magazines", magazineRepository.findAllMagazines());
        return "magazine/magazineList";
    }

    @Operation(summary = "Get a magazine by ID", description = "Returns the HTML view containing the selected magazine's details")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved magazine details")
    @GetMapping("/{id}")
    public String getMagazineById(@PathVariable Long id, Model model) {
        model.addAttribute("magazine", magazineRepository.findById(id).orElse(null));
        return "magazine/magazineDetails";
    }

    @Operation(summary = "Show the add magazine form", description = "Returns the HTML form used to create a magazine")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the add magazine form")
    @GetMapping("/add")
    public String addMagazineForm(Model model) {
        model.addAttribute("magazine", new MagazineEntity());
        return "magazine/addMagazine";
    }

    @Operation(summary = "Add a magazine", description = "Creates a magazine and redirects to the magazine list")
    @ApiResponse(responseCode = "302", description = "Magazine created; redirecting to the magazine list")
    @PostMapping("/add")
    public String addMagazine(@ModelAttribute MagazineEntity magazine) {
        magazineRepository.save(magazine);
        return "redirect:/magazines";
    }

    @Operation(summary = "Show the edit magazine form", description = "Returns the HTML form used to edit the selected magazine")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the edit magazine form")
    @GetMapping("/edit/{id}")
    public String editMagazineForm(@PathVariable Long id, Model model) {
        model.addAttribute("magazine", magazineRepository.findById(id).orElse(null));
        return "magazine/editMagazine";
    }

    @Operation(summary = "Edit a magazine", description = "Updates the selected magazine and redirects to the magazine list")
    @ApiResponse(responseCode = "302", description = "Magazine updated; redirecting to the magazine list")
    @PostMapping("/edit/{id}")
    public String editMagazine(@PathVariable Long id, @ModelAttribute MagazineEntity updatedMagazine) {
        magazineRepository.findById(id).ifPresent(magazine -> {
            magazine.setTitle(updatedMagazine.getTitle());
            magazine.setPrice(updatedMagazine.getPrice());
            magazine.setCopies(updatedMagazine.getCopies());
            magazine.setOrderQty(updatedMagazine.getOrderQty());
            magazine.setCurrentIssue(updatedMagazine.getCurrentIssue());
            magazineRepository.save(magazine);
        });
        return "redirect:/magazines";
    }

    @Operation(summary = "Delete a magazine", description = "Deletes the selected magazine and redirects to the magazine list")
    @ApiResponse(responseCode = "302", description = "Magazine deleted; redirecting to the magazine list")
    @GetMapping("/delete/{id}")
    public String deleteMagazine(@PathVariable Long id) {
        magazineRepository.findById(id).ifPresent(magazine -> {
            for (CartEntity cart : magazine.getCarts()) {
                cart.getProducts().remove(magazine);
                cartRepository.save(cart);
            }
            magazineRepository.delete(magazine);
        });
        return "redirect:/magazines";
    }
}

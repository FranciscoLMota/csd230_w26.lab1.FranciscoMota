package csd230.lab1.controllers;

import csd230.lab1.entities.CartEntity;
import csd230.lab1.entities.MagazineEntity;
import csd230.lab1.repositories.CartEntityRepository;
import csd230.lab1.repositories.MagazineRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/magazines")
public class MagazineController {
    private final MagazineRepository magazineRepository;
    private final CartEntityRepository cartRepository;

    public MagazineController(MagazineRepository magazineRepository, CartEntityRepository cartRepository) {
        this.magazineRepository = magazineRepository;
        this.cartRepository = cartRepository;
    }

    @GetMapping
    public String getAllMagazines(Model model) {
        model.addAttribute("magazines", magazineRepository.findAllMagazines());
        return "magazine/magazineList";
    }

    @GetMapping("/{id}")
    public String getMagazineById(@PathVariable Long id, Model model) {
        model.addAttribute("magazine", magazineRepository.findById(id).orElse(null));
        return "magazine/magazineDetails";
    }

    @GetMapping("/add")
    public String addMagazineForm(Model model) {
        model.addAttribute("magazine", new MagazineEntity());
        return "magazine/addMagazine";
    }

    @PostMapping("/add")
    public String addMagazine(@ModelAttribute MagazineEntity magazine) {
        magazineRepository.save(magazine);
        return "redirect:/magazines";
    }

    @GetMapping("/edit/{id}")
    public String editMagazineForm(@PathVariable Long id, Model model) {
        model.addAttribute("magazine", magazineRepository.findById(id).orElse(null));
        return "magazine/editMagazine";
    }

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

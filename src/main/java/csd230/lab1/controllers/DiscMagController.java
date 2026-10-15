package csd230.lab1.controllers;

import csd230.lab1.entities.CartEntity;
import csd230.lab1.entities.DiscMagEntity;
import csd230.lab1.repositories.CartEntityRepository;
import csd230.lab1.repositories.DiscMagRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/disc-mags")
public class DiscMagController {
    private final DiscMagRepository discMagRepository;
    private final CartEntityRepository cartRepository;

    public DiscMagController(DiscMagRepository discMagRepository, CartEntityRepository cartRepository) {
        this.discMagRepository = discMagRepository;
        this.cartRepository = cartRepository;
    }

    @GetMapping
    public String getAllDiscMags(Model model) {
        model.addAttribute("discMags", discMagRepository.findAll());
        return "discMag/discMagList";
    }

    @GetMapping("/{id}")
    public String getDiscMagById(@PathVariable Long id, Model model) {
        model.addAttribute("discMag", discMagRepository.findById(id).orElse(null));
        return "discMag/discMagDetails";
    }

    @GetMapping("/add")
    public String addDiscMagForm(Model model) {
        model.addAttribute("discMag", new DiscMagEntity());
        return "discMag/addDiscMag";
    }

    @PostMapping("/add")
    public String addDiscMag(@ModelAttribute DiscMagEntity discMag) {
        discMagRepository.save(discMag);
        return "redirect:/disc-mags";
    }

    @GetMapping("/edit/{id}")
    public String editDiscMagForm(@PathVariable Long id, Model model) {
        model.addAttribute("discMag", discMagRepository.findById(id).orElse(null));
        return "discMag/editDiscMag";
    }

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

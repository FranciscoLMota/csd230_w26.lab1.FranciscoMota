package csd230.lab1.controllers;

import csd230.lab1.entities.CartEntity;
import csd230.lab1.entities.TicketEntity;
import csd230.lab1.repositories.CartEntityRepository;
import csd230.lab1.repositories.TicketRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tickets")
public class TicketController {
    private final TicketRepository ticketRepository;
    private final CartEntityRepository cartRepository;

    public TicketController(TicketRepository ticketRepository, CartEntityRepository cartRepository) {
        this.ticketRepository = ticketRepository;
        this.cartRepository = cartRepository;
    }

    @GetMapping
    public String getAllTickets(Model model) {
        model.addAttribute("tickets", ticketRepository.findAll());
        return "ticket/ticketList";
    }

    @GetMapping("/{id}")
    public String getTicketById(@PathVariable Long id, Model model) {
        model.addAttribute("ticket", ticketRepository.findById(id).orElse(null));
        return "ticket/ticketDetails";
    }

    @GetMapping("/add")
    public String addTicketForm(Model model) {
        model.addAttribute("ticket", new TicketEntity());
        return "ticket/addTicket";
    }

    @PostMapping("/add")
    public String addTicket(@ModelAttribute TicketEntity ticket) {
        ticketRepository.save(ticket);
        return "redirect:/tickets";
    }

    @GetMapping("/edit/{id}")
    public String editTicketForm(@PathVariable Long id, Model model) {
        model.addAttribute("ticket", ticketRepository.findById(id).orElse(null));
        return "ticket/editTicket";
    }

    @PostMapping("/edit/{id}")
    public String editTicket(@PathVariable Long id, @ModelAttribute TicketEntity updatedTicket) {
        ticketRepository.findById(id).ifPresent(ticket -> {
            ticket.setDescription(updatedTicket.getDescription());
            ticket.setPrice(updatedTicket.getPrice());
            ticketRepository.save(ticket);
        });
        return "redirect:/tickets";
    }

    @GetMapping("/delete/{id}")
    public String deleteTicket(@PathVariable Long id) {
        ticketRepository.findById(id).ifPresent(ticket -> {
            for (CartEntity cart : ticket.getCarts()) {
                cart.getProducts().remove(ticket);
                cartRepository.save(cart);
            }
            ticketRepository.delete(ticket);
        });
        return "redirect:/tickets";
    }
}

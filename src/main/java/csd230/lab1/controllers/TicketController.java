package csd230.lab1.controllers;

import csd230.lab1.entities.CartEntity;
import csd230.lab1.entities.TicketEntity;
import csd230.lab1.repositories.CartEntityRepository;
import csd230.lab1.repositories.TicketRepository;
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

@Tag(name = "Tickets", description = "Operations related to Ticket Management")
@Controller
@RequestMapping("/tickets")
public class TicketController {
    private final TicketRepository ticketRepository;
    private final CartEntityRepository cartRepository;

    public TicketController(TicketRepository ticketRepository, CartEntityRepository cartRepository) {
        this.ticketRepository = ticketRepository;
        this.cartRepository = cartRepository;
    }

    @Operation(summary = "Get all tickets", description = "Returns the HTML view of the ticket list")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved ticket list")
    @GetMapping
    public String getAllTickets(Model model) {
        model.addAttribute("tickets", ticketRepository.findAll());
        return "ticket/ticketList";
    }

    @Operation(summary = "Get a ticket by ID", description = "Returns the HTML view containing the selected ticket's details")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved ticket details")
    @GetMapping("/{id}")
    public String getTicketById(@PathVariable Long id, Model model) {
        model.addAttribute("ticket", ticketRepository.findById(id).orElse(null));
        return "ticket/ticketDetails";
    }

    @Operation(summary = "Show the add ticket form", description = "Returns the HTML form used to create a ticket")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the add ticket form")
    @GetMapping("/add")
    public String addTicketForm(Model model) {
        model.addAttribute("ticket", new TicketEntity());
        return "ticket/addTicket";
    }

    @Operation(summary = "Add a ticket", description = "Creates a ticket and redirects to the ticket list")
    @ApiResponse(responseCode = "302", description = "Ticket created; redirecting to the ticket list")
    @PostMapping("/add")
    public String addTicket(@ModelAttribute TicketEntity ticket) {
        ticketRepository.save(ticket);
        return "redirect:/tickets";
    }

    @Operation(summary = "Show the edit ticket form", description = "Returns the HTML form used to edit the selected ticket")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the edit ticket form")
    @GetMapping("/edit/{id}")
    public String editTicketForm(@PathVariable Long id, Model model) {
        model.addAttribute("ticket", ticketRepository.findById(id).orElse(null));
        return "ticket/editTicket";
    }

    @Operation(summary = "Edit a ticket", description = "Updates the selected ticket and redirects to the ticket list")
    @ApiResponse(responseCode = "302", description = "Ticket updated; redirecting to the ticket list")
    @PostMapping("/edit/{id}")
    public String editTicket(@PathVariable Long id, @ModelAttribute TicketEntity updatedTicket) {
        ticketRepository.findById(id).ifPresent(ticket -> {
            ticket.setDescription(updatedTicket.getDescription());
            ticket.setPrice(updatedTicket.getPrice());
            ticketRepository.save(ticket);
        });
        return "redirect:/tickets";
    }

    @Operation(summary = "Delete a ticket", description = "Deletes the selected ticket and redirects to the ticket list")
    @ApiResponse(responseCode = "302", description = "Ticket deleted; redirecting to the ticket list")
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

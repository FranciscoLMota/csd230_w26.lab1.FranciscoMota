package csd230.lab1.controllers;

import csd230.lab1.entities.TicketEntity;
import csd230.lab1.repositories.TicketRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Ticket REST API", description = "JSON API for managing tickets")
@RestController
@RequestMapping("/api/rest/tickets")
public class TicketRestController {
    private final TicketRepository repository;

    public TicketRestController(TicketRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @Operation(summary = "Get all tickets")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all tickets")
    public List<TicketEntity> all() {
        return repository.findAll();
    }

    @PostMapping
    @Operation(summary = "Create a ticket")
    @ApiResponse(responseCode = "200", description = "Ticket created successfully")
    public TicketEntity newTicket(@RequestBody TicketEntity newTicket) {
        return repository.save(newTicket);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a ticket by ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the ticket")
    @ApiResponse(responseCode = "404", description = "Ticket not found")
    public TicketEntity one(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Replace a ticket by ID")
    @ApiResponse(responseCode = "200", description = "Ticket replaced successfully")
    public TicketEntity replaceTicket(@RequestBody TicketEntity newTicket,
                                       @PathVariable Long id) {
        return repository.findById(id)
                .map(ticket -> {
                    ticket.setDescription(newTicket.getDescription());
                    ticket.setPrice(newTicket.getPrice());
                    return repository.save(ticket);
                })
                .orElseGet(() -> {
                    newTicket.setId(id);
                    return repository.save(newTicket);
                });
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a ticket by ID")
    @ApiResponse(responseCode = "200", description = "Ticket deleted successfully")
    public void deleteTicket(@PathVariable Long id) {
        repository.deleteById(id);
    }
}

package com.oris.flight.controller;

import com.oris.flight.service.FlightSearchOrchestratorService;
import com.oris.search.request.FlightRequest;
import com.oris.search.response.FlightSearchResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/flights")
@RequiredArgsConstructor
public class FlightViewController {

    private final FlightSearchOrchestratorService service;

    @GetMapping("/{flightId}")
    public String flightDetailPage(
            @PathVariable UUID flightId,
            @Valid @ModelAttribute FlightRequest request,
            @AuthenticationPrincipal UUID accountId,
            Model model
    ) {
        FlightSearchResponse flight = service.getFlight(flightId, request);
        model.addAttribute("flight", flight);
        model.addAttribute("isAuthenticated", accountId != null);
        return "flights/flight";
    }
}

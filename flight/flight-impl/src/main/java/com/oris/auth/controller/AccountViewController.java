package com.oris.auth.controller;

import com.oris.auth.response.AccountResponse;
import com.oris.auth.service.AuthServiceClient;
import com.oris.search.request.dto.TicketStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.UUID;

@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountViewController {

    private final AuthServiceClient authServiceClient;

    @GetMapping
    public String profilePage(
            @AuthenticationPrincipal UUID accountId,
            Model model
    ) {
        AccountResponse account = authServiceClient.findAccountById(accountId);
        model.addAttribute("account",          account);
        model.addAttribute("isAuthenticated",  true);
        model.addAttribute("isAdmin",
                SecurityContextHolder.getContext().getAuthentication()
                        .getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        return "account/profile";
    }

    @GetMapping("/reservations")
    public String reservationsPage(
            @AuthenticationPrincipal UUID accountId,
            Model model
    ) {
        model.addAttribute("ticketStatuses",  Arrays.asList(TicketStatus.values()));
        model.addAttribute("isAuthenticated", true);
        return "account/reservations";
    }
}

package com.arsaka.auth.controller;

import com.arsaka.auth.response.AccountResponse;
import com.arsaka.auth.service.AuthServiceClient;
import com.arsaka.search.request.dto.TicketStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
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

    @PatchMapping("/me/username")
    @ResponseBody
    public ResponseEntity<AccountResponse> updateUsername(
            @AuthenticationPrincipal UUID accountId,
            @RequestBody Map<String, String> body
    ) {
        AccountResponse updated = authServiceClient.updateUsername(accountId, body.get("username"));
        return ResponseEntity.ok(updated);
    }
}

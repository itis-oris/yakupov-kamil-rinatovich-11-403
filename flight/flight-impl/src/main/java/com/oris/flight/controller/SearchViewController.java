package com.oris.flight.controller;

import com.oris.common.CabinClass;
import com.oris.referencedata.service.CityService;
import com.oris.search.request.dto.OrderType;
import com.oris.search.request.dto.TimeType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class SearchViewController {

    private final CityService cityService;

    @GetMapping("/")
    public String searchPage(
            @AuthenticationPrincipal UUID accountId,
            Model model
    ) {
        model.addAttribute("cities",          cityService.findAll());
        model.addAttribute("cabinClasses",    Arrays.asList(CabinClass.values()));
        model.addAttribute("orderTypes",      Arrays.asList(OrderType.values()));
        model.addAttribute("timeTypes",       Arrays.asList(TimeType.values()));
        model.addAttribute("isAuthenticated", accountId != null);
        return "flights/search";
    }
}

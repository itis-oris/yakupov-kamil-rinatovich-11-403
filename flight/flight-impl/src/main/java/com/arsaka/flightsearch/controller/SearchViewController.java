package com.arsaka.flightsearch.controller;

import com.arsaka.common.CabinClass;
import com.arsaka.referencedata.service.CityService;
import com.arsaka.search.request.dto.OrderType;
import com.arsaka.search.request.dto.TimeType;
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

package com.arsaka.flightsearch.controller;

import com.arsaka.common.CabinClass;
import com.arsaka.referencedata.service.CityService;
import com.arsaka.search.request.dto.OrderType;
import com.arsaka.search.request.dto.TimeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class FlightViewController {

    private final CityService cityService;

    @GetMapping("/")
    public String searchPage(Model model) {
        model.addAttribute("cabinClasses", CabinClass.values());
        model.addAttribute("orderTypes",   OrderType.values());
        model.addAttribute("timeTypes",    TimeType.values());
        model.addAttribute("cities",       cityService.findAll());
        return "flights/search";
    }


}

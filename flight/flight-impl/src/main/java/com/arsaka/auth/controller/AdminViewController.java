package com.arsaka.auth.controller;

import com.arsaka.auth.response.AccountResponse;
import com.arsaka.auth.service.AuthServiceClient;
import com.arsaka.common.*;
import com.arsaka.referencedata.service.AirportService;
import com.arsaka.referencedata.service.AirplaneTypeService;
import com.arsaka.referencedata.service.CountryService;
import com.arsaka.referencedata.service.RouteAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminViewController {

    private final AuthServiceClient authServiceClient;
    private final AirplaneTypeService airplaneTypeService;
    private final CountryService countryService;
    private final AirportService aIrportService;

    @GetMapping
    public String adminPage(
            @AuthenticationPrincipal UUID accountId,
            Model model
    ) {

        AccountResponse account = authServiceClient.findAccountById(accountId);

        model.addAttribute("airplaneTypes", airplaneTypeService.findAll());
        model.addAttribute("countries", countryService.findAll());
        model.addAttribute("airports", aIrportService.findAll());
        model.addAttribute("cabinClasses", Arrays.asList(CabinClass.values()));
        model.addAttribute("flightStatuses", Arrays.asList(FlightStatus.values()));
        model.addAttribute("passengerTypes", Arrays.asList(PassengerType.values()));
        model.addAttribute("pricingAdjTypes", Arrays.asList(PricingAdjustmentType.values()));
        model.addAttribute("account",         account);
        model.addAttribute("isAuthenticated", true);
        return "account/admin";
    }
}

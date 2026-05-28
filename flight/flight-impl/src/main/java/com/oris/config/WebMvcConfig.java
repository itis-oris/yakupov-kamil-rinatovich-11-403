package com.oris.config;

import com.oris.common.CabinClass;
import com.oris.common.FlightStatus;
import com.oris.common.PassengerType;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(String.class, PassengerType.class,
                PassengerType::fromValue);

        registry.addConverter(String.class, FlightStatus.class,
                FlightStatus::fromValue);

        registry.addConverter(String.class, CabinClass.class,
                CabinClass::fromValue);
    }
}
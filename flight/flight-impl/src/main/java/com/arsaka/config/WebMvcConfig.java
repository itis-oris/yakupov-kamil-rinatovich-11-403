package com.arsaka.config;

import com.arsaka.common.CabinClass;
import com.arsaka.common.FlightStatus;
import com.arsaka.common.PassengerType;
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
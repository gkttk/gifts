package com.epam.esm.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackages = {"com.epam.esm.assemblers","com.epam.esm.uri_builder"})
public class WebTestConfig implements WebMvcConfigurer {


}

package com.example.shop.app.configurations;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfigurations {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}

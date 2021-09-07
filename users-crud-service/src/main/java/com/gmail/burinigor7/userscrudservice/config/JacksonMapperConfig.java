package com.gmail.burinigor7.userscrudservice.config;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;

@Configuration
@AllArgsConstructor
public class JacksonMapperConfig {
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void configureObjectMapper() {
        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
    }
}

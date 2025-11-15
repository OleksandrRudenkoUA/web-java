package com.cosmocats.marketplace.config;

import com.cosmocats.marketplace.mapper.ProductMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MappersTestConfiguration {

    @Bean
    public ProductMapper getProductMapper() {
        return Mappers.getMapper(ProductMapper.class);
    }
}

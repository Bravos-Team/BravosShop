package com.bravos2k5.bravosshop.config;

import com.bravos2k5.bravosshop.utils.CurrencyFormatter;
import com.bravos2k5.bravosshop.utils.IdentifyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public IdentifyGenerator identifyGenerator() {
        return new IdentifyGenerator(1);
    }

    @Bean
    public CurrencyFormatter currencyFormatter() {
        return new CurrencyFormatter();
    }

}

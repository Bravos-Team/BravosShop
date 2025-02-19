package com.bravos2k5.bravosshop.common.config;

import com.bravos2k5.bravosshop.common.utils.CurrencyFormatter;
import com.bravos2k5.bravosshop.common.utils.IdentifyGenerator;
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

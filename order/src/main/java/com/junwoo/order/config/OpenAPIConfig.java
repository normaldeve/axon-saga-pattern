package com.junwoo.order.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 6.
 */
@Configuration
public class OpenAPIConfig {

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("Order API")
                        .description("Order Application")
                        .version("v0.0.1")
                );
    }
}

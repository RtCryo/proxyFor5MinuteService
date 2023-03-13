package com.example.proxyfor5minutservice.config;

import com.example.proxyfor5minutservice.CustomerClient;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.logging.Logger;

@Configuration
public class ApplicationConfig {

    Logger logger = Logger.getLogger("c.e.p.ApplicationListener");

    @Bean
    CustomerClient customerClient(WebClient.Builder builder) {
        return HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(builder.baseUrl("http://localhost:8080").build()))
                .build()
                .createClient(CustomerClient.class);
    }

    @Bean
    ApplicationListener<ApplicationReadyEvent> readyEventApplicationListener(CustomerClient customerClient) {
        return event -> customerClient.all().subscribe(customer -> logger.info(customer.toString()));
    }

    @Bean
    RouteLocator gateway(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(spec -> spec.path("/proxy")
                        .filters(filterSpec -> filterSpec.setPath("/customers"))
                        .uri("http://localhost:8080/"))
                .build();
    }

}

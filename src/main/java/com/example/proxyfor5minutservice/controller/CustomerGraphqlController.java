package com.example.proxyfor5minutservice.controller;

import com.example.proxyfor5minutservice.CustomerClient;
import com.example.proxyfor5minutservice.model.Customer;
import com.example.proxyfor5minutservice.model.Profile;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class CustomerGraphqlController {

    private final CustomerClient customerClient;

    public CustomerGraphqlController(final CustomerClient customerClient) {
        this.customerClient = customerClient;
    }

    @QueryMapping
    Flux<Customer> customers() {
        return this.customerClient.all();
    }

    @QueryMapping
    Flux<Customer> customerByName(@Argument String name) {
        return this.customerClient.byName(name);
    }

    @SchemaMapping (typeName = "Customer")
    Mono<Profile> profile (Customer customer) {
        return Mono.just(new Profile(customer.id()*10));
    }

}

package com.ccsw.tutorialloan.model.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "SPRING-CLOUD-EUREKA-CLIENT-CLIENT")
public interface ClientClient {

    @GetMapping(value = "/client")
    List<ClientDto> findAll();
}
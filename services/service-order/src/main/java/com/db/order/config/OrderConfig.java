package com.db.order.config;

import feign.Logger;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OrderConfig {
  @Bean
  @LoadBalanced//负载均衡
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  Logger.Level feignLoggerLevel() {
    return Logger.Level.FULL;
  }

//  @Bean
//  Retryer retryer() {
//    return new Retryer.Default();
//  }
}

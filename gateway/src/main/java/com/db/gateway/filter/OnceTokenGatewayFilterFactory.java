package com.db.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;


@Component
public class OnceTokenGatewayFilterFactory extends AbstractNameValueGatewayFilterFactory {
  @Override
  public GatewayFilter apply(NameValueConfig config) {
    return (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {
      ServerHttpResponse response = exchange.getResponse();

      String value = switch (config.getValue().toLowerCase()) {
        case "uuid" -> UUID.randomUUID().toString();
        case "jwt" -> "Test Token";
        default -> "";
      };

      HttpHeaders headers = response.getHeaders();
      headers.add(config.getName(), value);
    }));
  }
}

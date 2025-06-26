package com.db.order.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class XTokenRequestIntercepter implements RequestInterceptor {
  @Override
  public void apply(RequestTemplate requestTemplate) {
    requestTemplate.header("X-Token", UUID.randomUUID().toString());
  }
}

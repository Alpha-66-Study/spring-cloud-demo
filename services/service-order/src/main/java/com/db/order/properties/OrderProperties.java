package com.db.order.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(value = "order")
@Data
public class OrderProperties {
  String timeout;
  String autoConfirm;
  String dbUrl;
}

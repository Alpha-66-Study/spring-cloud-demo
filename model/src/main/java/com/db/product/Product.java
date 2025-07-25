package com.db.product;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Product {
  private Long id;
  private BigDecimal price;
  private String productName;
  private int num;
}

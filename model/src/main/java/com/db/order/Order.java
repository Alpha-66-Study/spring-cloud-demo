package com.db.order;

import com.db.product.Product;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Order {
  private Long id;
  private BigDecimal totalAmount;
  private Long userId;
  private String nickName;
  private String address;
  private List<Product> productList;
}

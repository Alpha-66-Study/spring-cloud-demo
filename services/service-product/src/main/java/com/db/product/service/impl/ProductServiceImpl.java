package com.db.product.service.impl;

import com.db.product.Product;
import com.db.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
  @Override
  public Product getProduct(Long productId) {
//    try {
//      TimeUnit.SECONDS.sleep(100);
//    } catch (InterruptedException e) {
//      throw new RuntimeException(e);
//    }
    log.info("getProduct:{}", productId);
    Product product = new Product();
    product.setId(productId);
    product.setPrice(new BigDecimal("7599"));
    product.setProductName("phone16ProMax" + productId);
    product.setNum(1);
    return product;
  }
}

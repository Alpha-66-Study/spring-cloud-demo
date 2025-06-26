package com.db.order.feign.fallback;

import com.db.order.feign.ProductFeignClient;
import com.db.product.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductFeignClientFallback implements ProductFeignClient {
  @Override
  public Product getProductById(Long productId) {
    System.out.println("兜底回调....");
    Product product = new Product();
    product.setId(productId);
    product.setPrice(new BigDecimal("0"));
    product.setProductName("未知商品");
    product.setNum(0);

    return product;
  }
}

package com.db.order.feign;

import com.db.order.feign.fallback.ProductFeignClientFallback;
import com.db.product.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "service-product", fallback = ProductFeignClientFallback.class)
public interface ProductFeignClient {
  /**
   * @GetMapping 当它们标记在 Controller 上时，用于接收请求
   * 当他们标记在 FeignClien 上时，用于发送请求
   */
  @GetMapping(value = "/product/{id}")
  public Product getProductById(@PathVariable("id") Long productId);
}

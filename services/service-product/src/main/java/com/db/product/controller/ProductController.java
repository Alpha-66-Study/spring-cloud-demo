package com.db.product.controller;

import com.db.product.Product;
import com.db.product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@AllArgsConstructor
public class ProductController {
  private final ProductService productService;

  @GetMapping(value = "/product/{id}")
  public Product getProductById(@PathVariable("id") Long productId, HttpServletRequest httpServletRequest) {
    String XToken = httpServletRequest.getHeader("X-Token");
    System.out.println("XToken = " + XToken);
    System.out.println("正在远程调用service-product...");
    //        int i = 10 / 0;
    try {
      TimeUnit.SECONDS.sleep(2);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    return productService.getProduct(productId);
  }

}

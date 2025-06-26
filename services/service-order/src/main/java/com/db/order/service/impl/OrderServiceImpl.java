package com.db.order.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.db.order.Order;
import com.db.order.feign.ProductFeignClient;
import com.db.order.service.OrderService;
import com.db.product.Product;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
  private final DiscoveryClient discoveryClient;
  private final LoadBalancerClient loadBalancerClient;
  private final RestTemplate restTemplate;
  @Qualifier("com.db.order.feign.ProductFeignClient")
  private final ProductFeignClient productFeignClient;

  @Override
  @SentinelResource(value = "createOrder", blockHandler = "createOrderFallback")
  public Order createOrder(Long userId, Long productId) {
    Order order = new Order();
    Product product = getProduct(productId);
    order.setId(1L);
    order.setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(product.getNum())));
    order.setUserId(userId);
    order.setNickName("手机");
    order.setAddress("家");
    ArrayList<Product> objects = new ArrayList<>();
    objects.add(getProduct(productId));
    order.setProductList(objects);
    return order;
  }

  public Order createOrderFallback(Long userId, Long productId, BlockException e) {
    Order order = new Order();
    order.setId(0L);
    order.setTotalAmount(new BigDecimal("0"));
    order.setUserId(userId);
    order.setNickName("未知用户");
    order.setAddress("异常信息" + e.getClass());
    return order;
  }

  private Product getProduct(Long productId) {
    //1.负载均衡 loadBalancerClient
//    ServiceInstance servi ceInstance = loadBalancerClient.choose("service-product");
    //2.基于注解的负载均衡@LoadBalanced
//    List<ServiceInstance> instances = discoveryClient.getInstances("service-product");
//    ServiceInstance serviceInstance = instances.get(0);
//    String url = "http://"+serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/product/" + productId;


//   String url = "http://service-product/product/" + productId;
//    log.info(" call:server-product, url:{}", url);
//    return restTemplate.getForObject(url, Product.class);
    return productFeignClient.getProductById(productId);
  }

}

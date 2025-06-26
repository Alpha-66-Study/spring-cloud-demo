package com.db.order.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.db.order.Order;
import com.db.order.properties.OrderProperties;
import com.db.order.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
@AllArgsConstructor
@Slf4j
public class OrderController {
  private final OrderService orderService;
  private final OrderProperties orderProperties;

  @GetMapping("/config")
  public String getConfig() {
    return orderProperties.getTimeout() + " : " + orderProperties.getAutoConfirm() + " : " + orderProperties.getDbUrl();
  }

  @GetMapping(value = "/create")
  @SentinelResource(value = "createOrder")
  public Order createOrder(@RequestParam("userId") Long userId, @RequestParam("productId") Long productId) {
    return orderService.createOrder(userId, productId);
  }

  @GetMapping("/seckill")
  @SentinelResource(value = "seckill-order", fallback = "seckillFallback")
  public Order seckill(@RequestParam(value = "userId", required = false) Long userId,
                       @RequestParam(value = "productId", defaultValue = "1000") Long productId) {
    Order order = orderService.createOrder(productId, userId);
    order.setId(Long.MAX_VALUE);
    return order;
  }

  public Order seckillFallback(Long userId,
                               Long productId,
                               // 使用 fallback，而不是 blockHandler
                               // 最后一个参数类型是 Throwable，而不是 BlockException
                               Throwable throwable) {
    System.out.println("seckillFallback...");
    Order order = new Order();
    order.setId(productId);
    order.setUserId(userId);
    order.setAddress("异常信息: " + throwable.getClass());
    return order;
  }

  //关联
  @GetMapping("/writeDb")
  public String writeDb() {
    return "writeDb success...";
  }

  @GetMapping("/readDb")
  public String readDb() {
    log.info("readDb success...");
    return "readDb success...";
  }


}

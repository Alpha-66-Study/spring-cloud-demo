package com.db.order.service;

import com.db.order.Order;

public interface OrderService {
  Order createOrder(Long userId, Long productId);

}

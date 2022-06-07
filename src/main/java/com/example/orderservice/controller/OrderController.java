package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;
import com.google.common.collect.Lists;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order-service")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;
  private final Environment env;

  @GetMapping("/heath_check")
  public String status() {
    return String.format("It's Working in Order Service on Port %s.",
        env.getProperty("local.server.port"));
  }

  @PostMapping("/{userId}/orders")
  public ResponseEntity<ResponseOrder> createUser(@PathVariable String userId, @RequestBody RequestOrder requestOrder) {
    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    OrderDto orderDto = mapper.map(requestOrder, OrderDto.class);
    orderDto.setUserId(userId);
    OrderDto createOrder = orderService.createOrder(orderDto);

    ResponseOrder responseOrder = mapper.map(createOrder, ResponseOrder.class);
    return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
  }

  @GetMapping("/{userId}/orders")
  ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable String userId) {
    Iterable<OrderEntity> orders = orderService.getOrderSByUserId(userId);
    List<ResponseOrder> responseOrders = Lists.newArrayList();
    orders.forEach(v-> responseOrders.add(new ModelMapper().map(v, ResponseOrder.class)));

    return ResponseEntity.status(HttpStatus.OK).body(responseOrders);

  }
}

package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.jpa.OrderRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;

  public OrderDto createOrder(OrderDto orderDto) {
    orderDto.setOrderId(UUID.randomUUID().toString());
    orderDto.setTotalPrice(orderDto.getUnitPrice() * orderDto.getQty());

    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    OrderEntity orderEntity = mapper.map(orderDto, OrderEntity.class);

    orderRepository.save(orderEntity);

    return mapper.map(orderEntity, OrderDto.class);
  }

  public Iterable<OrderEntity> getOrderSByUserId(String userId) {
    return orderRepository.findByUserId(userId);
  }
  public OrderDto getOrderByOrderId(String orderId) {
    OrderEntity orderEntity = orderRepository.findByOrderId(orderId);
    return new ModelMapper().map(orderEntity, OrderDto.class);
  }

}

package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.responses.OrderResponse;

import java.util.List;

public interface IOrderService {

    OrderResponse createOrder(OrderDTO orderDTO) throws Exception;

    OrderResponse getOrder(Long id) throws Exception;

    OrderResponse updateOrder(Long id, OrderDTO orderDTO) throws Exception;

    void deleteOrder(Long id);

    List<OrderResponse> findByUserId(Long userId) throws Exception;
}

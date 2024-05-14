package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderStatus;
import com.project.shopapp.models.User;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.UserRepository;
import com.project.shopapp.responses.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public OrderResponse createOrder(OrderDTO orderDTO) throws Exception {
        User user = userRepository
                .findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with ID:" + orderDTO.getUserId()));

        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if(shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFoundException("Date must be at least today!");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);
        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    public OrderResponse getOrder(Long id) throws Exception {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find order with ID: " + id));
        return modelMapper.map(existingOrder, OrderResponse.class);
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderDTO orderDTO) throws Exception {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find order with ID:" + id));
        User user = userRepository
                .findById(order.getUser().getId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with ID:" + order.getUser().getId()));
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        orderRepository.save(order);
        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setActive(false);
            orderRepository.save(order);
        }
    }

    @Override
    public List<OrderResponse> findByUserId(Long userId) throws Exception {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with ID:" + userId));
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderResponse.class))
                .toList();
    }
}

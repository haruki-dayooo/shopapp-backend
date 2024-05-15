package com.project.shopapp.services;


import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.models.Product;
import com.project.shopapp.repositories.OrderDetailRepository;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {

    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception{
        Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find order with ID:"
                        + orderDetailDTO.getOrderId()));

        Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with ID:"
                        + orderDetailDTO.getProductId()));

        OrderDetail orderDetail = OrderDetail.builder()
                .order(existingOrder)
                .product(existingProduct)
                .price(orderDetailDTO.getPrice())
                .numberOfProducts(Math.toIntExact(orderDetailDTO.getNumberOfProducts()))
                .totalMoney(orderDetailDTO.getTotalMoney())
                .color(orderDetailDTO.getColor())
                .build();

        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail getOrderDetail(Long id) throws Exception{
        return orderDetailRepository.findById(id).
                orElseThrow(() -> new DataNotFoundException("Cannot find order detail with ID: " + id));
    }

    @Override
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws Exception {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find order detail with ID: " + id));

        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find order detail with order ID: "
                        + orderDetailDTO.getOrderId()));

        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with ID:"
                        + orderDetailDTO.getProductId()));
        orderDetail.setOrder(order);
        orderDetail.setProduct(product);
        orderDetail.setPrice(orderDetailDTO.getPrice());
        orderDetail.setNumberOfProducts(Math.toIntExact(orderDetailDTO.getNumberOfProducts()));
        orderDetail.setTotalMoney(orderDetail.getTotalMoney());
        orderDetail.setColor(orderDetailDTO.getColor());
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public void deleteOrderDetail(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetail> findByOrderId(Long id) {
        return orderDetailRepository.findByOrderId(id);
    }
}

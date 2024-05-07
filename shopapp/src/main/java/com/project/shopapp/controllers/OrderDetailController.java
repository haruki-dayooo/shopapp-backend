package com.project.shopapp.controllers;

import com.project.shopapp.dtos.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {

    @PostMapping("/create")
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO orderDetail,
            BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }

            return ResponseEntity.ok("Create order detail successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getOrderDetail(@Valid @PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok("Get order detail with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/order/{order_id}")
    public ResponseEntity<String> getOrderDetails(@Valid @PathVariable("order_id") Long orderId) {
        try {
            return ResponseEntity.ok("Get all order details with Order ID: " + orderId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateOrderDetail(
            @Valid @PathVariable Long id,
            @Valid @RequestBody OrderDetailDTO orderDetail) {
        return ResponseEntity.ok("Update order with ID: " + id + ". New order detail: " + orderDetail);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderDetail(@Valid @PathVariable Long id) {
        return ResponseEntity.ok("Order deleted successfully");
    }
}

package com.example.WebBanSach.services;

import com.example.WebBanSach.entity.CartItem;
import com.example.WebBanSach.entity.Order;
import com.example.WebBanSach.entity.OrderDetail;
import com.example.WebBanSach.repository.ProductRepository;
import com.example.WebBanSach.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    @Autowired
    private final OrderRepository orderRepository;

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    private final ProductRepository productRepository;

    public void createOrder(Order order, List<CartItem> cartItems) {
        // Thiết lập thông tin của đơn hàng từ đối tượng Order được truyền vào
        orderRepository.save(order);

        for (CartItem cartItem : cartItems) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(cartItem.getProduct());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setOrder(order);


            order.getOrderDetails().add(orderDetail);
        }

        orderRepository.save(order);
    }
}


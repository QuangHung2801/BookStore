package com.example.WebBanSach.repository;

import com.example.WebBanSach.entity.Order;
import com.example.WebBanSach.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);

}
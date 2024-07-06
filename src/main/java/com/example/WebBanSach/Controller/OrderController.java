package com.example.WebBanSach.Controller;

import com.example.WebBanSach.entity.*;
import com.example.WebBanSach.services.CartService;
import com.example.WebBanSach.services.OrderService;
import com.example.WebBanSach.services.ProductService; // Add this import
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService; // Add this field

    @GetMapping("/checkout")
    public String checkout() {
        return "/cart/checkout";
    }

    @PostMapping("/submit")
    public String submitOrder(@RequestParam String customerName,
                              @RequestParam String shippingAddress,
                              @RequestParam String phoneNumber,
                              @RequestParam String email,
                              @RequestParam String notes,
                              @RequestParam String paymentMethod,
                              Model model) {

        List<CartItem> cartItems = cartService.getCartItems();
        double totalPrice = cartService.calculateTotalPrice(cartItems);

        Order order = new Order();
        order.setCustomerName(customerName);
        order.setShippingAddress(shippingAddress);
        order.setPhoneNumber(phoneNumber);
        order.setEmail(email);
        order.setNotes(notes);
        order.setPaymentMethod(paymentMethod);
        order.setTotalPrice(totalPrice);
        order.setOrderDate(new Date());

        int totalQuantity = cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            int quantity = item.getQuantity();

            if (product.getQuantity() < quantity) {
                model.addAttribute("error", "Sản phẩm " + product.getTitle() + " không đủ số lượng trong kho.");
                model.addAttribute("cartItems", cartItems);
                return "cart/checkout";
            }

            product.setQuantity(product.getQuantity() - quantity);
            productService.updateProduct(product);

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(product);
            orderDetail.setQuantity(quantity);
            orderDetail.setOrder(order);
            orderDetail.setPaymentMethod(paymentMethod);
            orderDetail.setShippingAddress(shippingAddress);
            orderDetail.setPhoneNumber(phoneNumber);

            order.getOrderDetails().add(orderDetail);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetail) {
            CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
            orderService.saveOrder(order, customUserDetail.getUser());
        }

        cartService.clearCart();

        return "redirect:/order/confirmation";
    }



    @GetMapping("/confirmation")
    public String orderConfirmation(Model model) {
        model.addAttribute("message", "Your order has been successfully placed.");
        return "cart/order-confirmation";
    }



}

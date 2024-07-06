package com.example.WebBanSach.Controller;

import com.example.WebBanSach.entity.CartItem;
import com.example.WebBanSach.entity.Order;
import com.example.WebBanSach.entity.OrderDetail;
import com.example.WebBanSach.entity.Product;
import com.example.WebBanSach.services.CartService;
import com.example.WebBanSach.services.OrderService;
import com.example.WebBanSach.services.ProductService; // Add this import
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
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

        // Tính tổng số lượng sản phẩm
        int totalQuantity = cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        // Trừ số lượng sản phẩm trong kho và tạo OrderDetail
        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            int quantity = item.getQuantity();

            if (product.getQuantity() < quantity) {
                model.addAttribute("error", "Sản phẩm " + product.getTitle() + " không đủ số lượng trong kho.");
                model.addAttribute("cartItems", cartItems); // Truyền lại danh sách sản phẩm vào model để hiển thị lại
                return "cart/checkout"; // Trả về lại trang checkout với thông báo lỗi
            }

            product.setQuantity(product.getQuantity() - quantity);
            productService.updateProduct(product);

            // Tạo OrderDetail với quantity là số lượng trong giỏ hàng của sản phẩm
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(product);
            orderDetail.setQuantity(quantity);
            orderDetail.setOrder(order);
            orderDetail.setPaymentMethod(paymentMethod);
            orderDetail.setShippingAddress(shippingAddress);
            orderDetail.setPhoneNumber(phoneNumber);

            // Thêm OrderDetail vào Order
            order.getOrderDetails().add(orderDetail);
        }

        orderService.saveOrder(order);
        cartService.clearCart();

        return "redirect:/order/confirmation";
    }



    @GetMapping("/confirmation")
    public String orderConfirmation(Model model) {
        model.addAttribute("message", "Your order has been successfully placed.");
        return "cart/order-confirmation";
    }
}

package com.example.WebBanSach.Controller;

import com.example.WebBanSach.entity.CartItem;
import com.example.WebBanSach.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping
    public String showCart(Model model) {
        List<CartItem> cartItems = cartService.getCartItems();
        double totalPrice = cartService.calculateTotalPrice(cartItems);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);

        return "cart/cart";
    }

    @PostMapping("/update")
    public String updateCartQuantities(@RequestParam List<Long> ids, @RequestParam List<Integer> quantities) {
        for (int i = 0; i < ids.size(); i++) {
            Long itemId = ids.get(i);
            int quantity = quantities.get(i);
            cartService.updateCartItemQuantity(itemId, quantity);
        }
        return "redirect:/cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam int productId, @RequestParam int quantity) {
        cartService.addToCart(productId, quantity);
        return "redirect:/cart";
    }

    @GetMapping("/remove/{itemId}")
    public String removeFromCart(@PathVariable int itemId) {
        cartService.removeFromCart(itemId);
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        List<CartItem> cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        double totalPrice = cartService.calculateTotalPrice(cartItems);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);

        return "cart/checkout";
    }

    @GetMapping("/home")
    public String goToHome() {
        return "redirect:/Admin";
    }

}

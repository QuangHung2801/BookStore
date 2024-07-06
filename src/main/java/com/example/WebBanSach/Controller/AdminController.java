package com.example.WebBanSach.Controller;

import com.example.WebBanSach.entity.Category;
import com.example.WebBanSach.entity.Order;
import com.example.WebBanSach.entity.Product;
import com.example.WebBanSach.entity.User;
import com.example.WebBanSach.services.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/Admin")
public class AdminController {
    @Autowired
    private UserServices userService;

    @Autowired
    private ExcelService excelService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping()
    public String showAllProducts(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productService.getAllProducts(pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        return "/Admin/products/product";
    }

    @GetMapping("/categories/category")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showAllCategories(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "5") int size,
                                    Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categoryPage = categoryService.getAllCategories(pageable);

        model.addAttribute("categories", categoryPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categoryPage.getTotalPages());
        return "/Admin/categories/category";
    }

//    @GetMapping("/categories/category")
//    public String showAllCategories(Model model) {
//        List<Category> categories = categoryService.getAllCategories();
//        model.addAttribute("categories", categories);
//        return "/Admin/categories/category";
//    }

    @GetMapping("/products/add_product")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "/Admin/products/add_product";
    }

    @PostMapping("/products/add_product")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addProduct(@Valid @ModelAttribute("product") Product product,
                             @RequestParam("image") MultipartFile imageFile,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "/Admin/products/add_product";
        }

        if (!imageFile.isEmpty()) {
            try {
                String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
                product.setImg1(fileName);
                String uploadDir = "src/main/resources/static/Admin/img";
                FileUploadUtil.saveFile(uploadDir, fileName, imageFile);
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "Could not upload image.");
                return "/Admin/products/add_product";
            }
        }

        productService.addProduct(product);
        return "redirect:/Admin";
    }

    @GetMapping("/products/edit_product/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showEditProductForm(@PathVariable Long id, @RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("currentPage", page);
        return "/Admin/products/edit_product";
    }

    @PostMapping("/products/edit_product/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String updateProduct(@PathVariable Long id, @Valid @ModelAttribute("product") Product product,
                                @RequestParam(value = "image", required = false) MultipartFile imageFile,
                                @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("currentPage", page);
            return "/Admin/products/edit_product";
        }

        Product existingProduct = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));

        existingProduct.setTitle(product.getTitle());
        existingProduct.setAuthor(product.getAuthor());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setDetail(product.getDetail());
        existingProduct.setCategory(product.getCategory());

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
                String uploadDir = "src/main/resources/static/Admin/img";
                // Save new image
                FileUploadUtil.saveFile(uploadDir, fileName, imageFile);

                // Delete previous image if it exists
                if (existingProduct.getImg1() != null && !existingProduct.getImg1().isEmpty()) {
                    FileUploadUtil.deleteFile(uploadDir + existingProduct.getImg1());
                }

                existingProduct.setImg1(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "Could not upload image.");
                model.addAttribute("currentPage", page);
                return "/Admin/products/edit_product";
            }
        }

        productService.updateProduct(existingProduct);
        return "redirect:/Admin?page=" + page;
    }


    @GetMapping("/products/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/Admin";
    }

    @GetMapping("/categories/add_category")

    public String showAddCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "/Admin/categories/add_category";
    }

    @PostMapping("/categories/add_category")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addCategory(@ModelAttribute Category category) {
        categoryService.saveCategory(category);
        return "redirect:/Admin/categories/category";
    }

    @GetMapping("/categories/edit_category/{id}")
    public String showEditCategoryForm(@PathVariable Long id, Model model) {
        Category category = categoryService.getCategoryById(id);
        if (category != null) {
            model.addAttribute("category", category);
            return "/Admin/categories/edit_category";
        } else {
            return "redirect:/Admin/categories/category";
        }
    }

    @PostMapping("/categories/edit_category")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String editCategory(@ModelAttribute Category category) {
        categoryService.saveCategory(category);
        return "redirect:/Admin/categories/category";
    }

    @GetMapping("/categories/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/Admin/categories/category";
    }
    @PostMapping("/products/delete_multiple")
    public String deleteMultipleProducts(@RequestParam("productIds") List<Long> productIds, RedirectAttributes redirectAttributes) {
        if (productIds != null) {
            productService.deleteProductsByIds(productIds);
            redirectAttributes.addFlashAttribute("message", "Deleted selected products successfully.");
        } else {
            redirectAttributes.addFlashAttribute("message", "No products selected for deletion.");
        }
        return "redirect:/Admin";
    }

    @PostMapping("/categories/delete_multiple")
    public String deleteMultipleCategories(@RequestParam("categoryIds") List<Long> categoryIds, RedirectAttributes redirectAttributes) {
        if (categoryIds != null) {
            categoryService.deleteCategoryByIds(categoryIds);
            redirectAttributes.addFlashAttribute("message", "Deleted selected categories successfully.");
        } else {
            redirectAttributes.addFlashAttribute("message", "No categories selected for deletion.");
        }
        return "redirect:/Admin/categories/category";
    }


    @GetMapping("/products/upload_excel")
    public String showUploadExcelForm(Model model) {
        return "/Admin/products/upload_excel";
    }

    @PostMapping("/products/upload_excel")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String uploadExcelFile(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("errorMessage", "Please select a file to upload.");
            return "/Admin/products/upload_excel";
        }

        try {
            // Parse the Excel file
            List<Product> products = excelService.parseExcelFile(file);

            // Save new products to the database
            productService.saveAll(products);

            return "redirect:/Admin";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Failed to upload file: " + e.getMessage());
            return "/Admin/products/upload_excel";
        }
    }

    @GetMapping("/products/export_excel")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void exportProductsToExcel(HttpServletResponse response) {
        try {
            excelService.exportProductsToExcel(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam("title") String title,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "5") int size,
                                 Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productService.searchProductsByTitle(title, pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("searchTitle", title);
        return "/Admin/products/product";
    }

    @GetMapping("/categories/category/search")
    public String searchCategories(@RequestParam("name") String name,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "5") int size,
                                 Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categoryPage = categoryService.searchCategorysByName(name, pageable);

        model.addAttribute("categories", categoryPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages",  categoryPage.getTotalPages());
        model.addAttribute("searchName", name);
        return "/Admin/categories/category";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "admin/users"; // Ensure this matches the Thymeleaf template name
    }


    @GetMapping("/manage-orders")
    public String manageOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "/Admin/manage-orders"; // Adjust this path if necessary
    }

    @GetMapping("/orders/{orderId}/update-status")
    public String showUpdateStatusForm(@PathVariable Long orderId, Model model) {
        Optional<Order> orderOptional = orderService.getOrderById(orderId);
        if (orderOptional.isPresent()) {
            model.addAttribute("order", orderOptional.get());
            return "admin/orders/update-status";
        } else {
            // Xử lý khi không tìm thấy đơn hàng
            return "redirect:/Admin/manage-orders"; // Hoặc hiển thị thông báo lỗi
        }
    }

    @PostMapping("/orders/{orderId}/update-status")
    public String updateOrderStatus(@PathVariable Long orderId,
                                    @RequestParam("status") String status) {
        Optional<Order> orderOptional = orderService.getOrderById(orderId);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();

            // Assuming you want to get the user associated with the order
            User user = order.getUser(); // Assuming there's a method to retrieve the user associated with the order

            order.setStatus(status);
            orderService.saveOrder(order, user); // Save the updated order with the associated user

            return "redirect:/Admin/manage-orders"; // Redirect to manage orders page after update
        } else {
            // Handle case where order is not found
            return "redirect:/Admin/manage-orders"; // Or display an error message
        }
    }

    @GetMapping("/orders/{orderId}/invoice")
    public String sendInvoice(@PathVariable Long orderId, RedirectAttributes redirectAttributes) {
        Optional<Order> orderOptional = orderService.getOrderById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();

            if ("delivered".equalsIgnoreCase(order.getStatus())) {
                try {
                    emailService.sendInvoiceEmail(order, order.getEmail());
                    redirectAttributes.addFlashAttribute("message", "Hóa đơn đã được gửi tới email của khách hàng.");
                } catch (MessagingException e) {
                    e.printStackTrace();
                    redirectAttributes.addFlashAttribute("errorMessage", "Không thể gửi hóa đơn: " + e.getMessage());
                }
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Trạng thái đơn hàng không phải là 'delivered'.");
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy đơn hàng.");
        }

        return "redirect:/Admin/manage-orders";
    }

}

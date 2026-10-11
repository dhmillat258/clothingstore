package com.clothingstore2.clothingstore2;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public AdminController(ProductRepository productRepository,
                           OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    private boolean isAdminLoggedIn(HttpSession session) {
        return Boolean.TRUE.equals(session.getAttribute("adminLoggedIn"));
    }

    @GetMapping
    public String dashboard(Model model, HttpSession session) {

        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        List<Product> products = productRepository.findAll();
        List<Order> orders = orderRepository.findAll();

        long lowStockCount = products.stream()
                .filter(product -> product.getStock() <= 5)
                .count();

        double totalSales = orders.stream()
                .mapToDouble(Order::getTotal)
                .sum();

        model.addAttribute("products", products);
        model.addAttribute("orders", orders);
        model.addAttribute("productCount", products.size());
        model.addAttribute("orderCount", orders.size());
        model.addAttribute("lowStockCount", lowStockCount);
        model.addAttribute("totalSales", totalSales);

        return "admin";
    }

    @GetMapping("/products")
    public String products(Model model, HttpSession session) {

        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        model.addAttribute("products", productRepository.findAll());

        return "admin-products";
    }

    @GetMapping("/products/add")
    public String addProductPage(Model model, HttpSession session) {

        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        model.addAttribute("product", new Product());

        return "admin-add-product";
    }

    @PostMapping("/products/save")
    public String saveProduct(
            @ModelAttribute Product product,
            HttpSession session,
            Model model) {

        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        String error = validateProduct(product);

        if (error != null) {

            model.addAttribute("error", error);
            model.addAttribute("product", product);

            if (product.getId() != null) {
                return "admin-edit-product";
            }

            return "admin-add-product";
        }

        product.setName(product.getName().trim());
        product.setCategory(product.getCategory().trim());
        product.setImage(product.getImage().trim());

        if (product.getColor() != null) {
            product.setColor(product.getColor().trim());
        }

        if (product.getStock() < 0) {
            product.setStock(0);
        }

        productRepository.save(product);

        return "redirect:/admin/products";
    }

    private String validateProduct(Product product) {

        if (product.getName() == null ||
                product.getName().trim().isEmpty()) {
            return "Product name is required.";
        }

        if (product.getName().trim().length() < 2) {
            return "Product name must contain at least 2 characters.";
        }

        if (product.getPrice() <= 0) {
            return "Price must be greater than 0.";
        }

        if (product.getCategory() == null ||
                product.getCategory().trim().isEmpty()) {
            return "Category is required.";
        }

        if (product.getImage() == null ||
                product.getImage().trim().isEmpty()) {
            return "Image path is required.";
        }

        if (product.getStock() < 0) {
            return "Stock cannot be negative.";
        }

        return null;
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(
            @PathVariable Long id,
            HttpSession session) {

        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        }

        return "redirect:/admin/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(
            @PathVariable Long id,
            Model model,
            HttpSession session) {

        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        Product product =
                productRepository.findById(id).orElse(null);

        if (product == null) {
            return "redirect:/admin/products";
        }

        model.addAttribute("product", product);

        return "admin-edit-product";
    }

    @GetMapping("/orders")
    public String orders(
            Model model,
            HttpSession session) {

        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        model.addAttribute("orders", orderRepository.findAll());

        return "admin-orders";
    }

    @PostMapping("/orders/status")
    public String updateOrderStatus(
            @RequestParam Long orderId,
            @RequestParam String status,
            HttpSession session) {

        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        Order order =
                orderRepository.findById(orderId).orElse(null);

        if (order != null) {

            if (status.equals("Pending") ||
                    status.equals("Confirmed") ||
                    status.equals("Shipped") ||
                    status.equals("Delivered") ||
                    status.equals("Cancelled")) {

                order.setStatus(status);
                orderRepository.save(order);
            }
        }

        return "redirect:/admin/orders";
    }
}
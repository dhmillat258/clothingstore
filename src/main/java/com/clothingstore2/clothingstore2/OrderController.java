package com.clothingstore2.clothingstore2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public OrderController(
            OrderRepository orderRepository,
            CustomerRepository customerRepository,
            OrderItemRepository orderItemRepository,
            ProductRepository productRepository) {

        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/checkout")
    public String checkout(HttpSession session) {

        Object customerId = session.getAttribute("customerId");

        if (customerId == null) {
            return "redirect:/login";
        }

        return "checkout";
    }

    @Transactional
    @PostMapping("/place-order")
    @ResponseBody
    public String placeOrder(
            @RequestParam String name,
            @RequestParam String phone,
            @RequestParam String email,
            @RequestParam String address,
            @RequestParam String payment,
            @RequestParam double total,
            @RequestParam(required = false) String cartItems,
            HttpSession session) {

        Object customerId =
                session.getAttribute("customerId");

        if (customerId == null) {
            return "LOGIN_REQUIRED";
        }

        Customer customer =
                customerRepository
                        .findById((Long) customerId)
                        .orElse(null);

        if (customer == null) {
            return "LOGIN_REQUIRED";
        }

        name = name.trim();
        phone = phone.trim();
        email = email.trim();
        address = address.trim();

        if (name.length() < 2) {
            return "INVALID_NAME";
        }

        if (!name.matches("[a-zA-Z ]+")) {
            return "INVALID_NAME";
        }

        if (!phone.matches("01[3-9][0-9]{8}")) {
            return "INVALID_PHONE";
        }

        if (!email.isEmpty() &&
                !email.matches(
                        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

            return "INVALID_EMAIL";
        }

        if (address.length() < 5) {
            return "INVALID_ADDRESS";
        }

        if (!payment.equals("Cash on Delivery") &&
                !payment.equals("Online Payment")) {

            return "INVALID_PAYMENT";
        }

        if (cartItems == null || cartItems.isBlank()) {
            return "error";
        }

        try {

            ObjectMapper mapper = new ObjectMapper();

            List<Map<String, Object>> items =
                    mapper.readValue(
                            cartItems,
                            new TypeReference<List<Map<String, Object>>>() {}
                    );

            if (items.isEmpty()) {
                return "error";
            }

            List<OrderItem> orderItems =
                    new ArrayList<>();

            double calculatedTotal = 0;

            for (Map<String, Object> item : items) {

                Long productId =
                        Long.parseLong(
                                String.valueOf(item.get("id")));

                int quantity =
                        Integer.parseInt(
                                String.valueOf(item.get("quantity")));

                if (quantity <= 0) {
                    return "error";
                }

                Product product =
                        productRepository
                                .findById(productId)
                                .orElse(null);

                if (product == null) {
                    return "error";
                }

                if (product.getStock() < quantity) {
                    return "OUT_OF_STOCK:" +
                            product.getName();
                }

                calculatedTotal +=
                        product.getPrice() * quantity;

                OrderItem orderItem =
                        new OrderItem();

                orderItem.setProductId(
                        product.getId());

                orderItem.setProductName(
                        product.getName());

                orderItem.setPrice(
                        product.getPrice());

                orderItem.setQuantity(quantity);

                orderItem.setImage(
                        product.getImage());

                orderItems.add(orderItem);
            }

            Order order = new Order();

            order.setCustomerName(name);
            order.setPhone(phone);
            order.setEmail(email);
            order.setAddress(address);
            order.setPaymentMethod(payment);

            order.setTotal(calculatedTotal);

            order.setStatus("Pending");

            order.setOrderDate(
                    LocalDateTime.now());

            order.setCustomer(customer);

            orderRepository.save(order);

            for (OrderItem orderItem : orderItems) {

                Product product =
                        productRepository
                                .findById(
                                        orderItem.getProductId())
                                .orElseThrow();

                product.setStock(
                        product.getStock()
                                - orderItem.getQuantity());

                productRepository.save(product);

                orderItem.setOrder(order);

                orderItemRepository.save(orderItem);
            }

            return "success:" + order.getId();

        } catch (Exception e) {

            e.printStackTrace();

            return "error";
        }
    }

    @GetMapping("/order-success")
    public String orderSuccess(
            @RequestParam Long order,
            HttpSession session,
            Model model) {

        Object customerId =
                session.getAttribute("customerId");

        if (customerId == null) {
            return "redirect:/login";
        }

        Order selectedOrder =
                orderRepository
                        .findById(order)
                        .orElse(null);

        if (selectedOrder == null) {
            return "redirect:/";
        }

        if (selectedOrder.getCustomer() == null ||
                !selectedOrder.getCustomer()
                        .getId()
                        .equals((Long) customerId)) {

            return "redirect:/order-history";
        }

        model.addAttribute(
                "order",
                selectedOrder);

        return "order-success";
    }

    @GetMapping("/order-history")
    public String orderHistory(
            HttpSession session,
            Model model) {

        Object customerId =
                session.getAttribute("customerId");

        if (customerId == null) {
            return "redirect:/login";
        }

        Customer customer =
                customerRepository
                        .findById((Long) customerId)
                        .orElse(null);

        if (customer == null) {
            return "redirect:/login";
        }

        List<Order> orders =
                orderRepository
                        .findByCustomer(customer);

        model.addAttribute(
                "orders",
                orders);

        return "order-history";
    }

    @GetMapping("/order-details/{id}")
    public String orderDetails(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        Object customerId =
                session.getAttribute("customerId");

        if (customerId == null) {
            return "redirect:/login";
        }

        Order order =
                orderRepository
                        .findById(id)
                        .orElse(null);

        if (order == null) {
            return "redirect:/order-history";
        }

        if (order.getCustomer() == null ||
                !order.getCustomer()
                        .getId()
                        .equals((Long) customerId)) {

            return "redirect:/order-history";
        }

        model.addAttribute(
                "order",
                order);

        return "order-details";
    }
}
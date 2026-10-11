package com.clothingstore2.clothingstore2;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CustomerController {

    private final CustomerRepository customerRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String phone,
            Model model) {

        name = name.trim();
        email = email.trim().toLowerCase();
        phone = phone.trim();

        if (name.isEmpty()) {
            model.addAttribute("error", "Please enter your full name.");
            return "signup";
        }

        if (name.length() < 2) {
            model.addAttribute("error", "Name must contain at least 2 characters.");
            return "signup";
        }

        if (!name.matches("[a-zA-Z ]+")) {
            model.addAttribute("error", "Name can contain only letters and spaces.");
            return "signup";
        }

        if (email.isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            model.addAttribute("error", "Please enter a valid email address.");
            return "signup";
        }

        if (phone.isEmpty() || !phone.matches("01[3-9][0-9]{8}")) {
            model.addAttribute("error",
                    "Please enter a valid 11-digit Bangladesh phone number.");
            return "signup";
        }

        if (password.length() < 6) {
            model.addAttribute("error",
                    "Password must be at least 6 characters.");
            return "signup";
        }

        if (customerRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error",
                    "Email already exists. Please use another email.");
            return "signup";
        }

        Customer customer = new Customer();

        customer.setName(name);
        customer.setEmail(email);

        customer.setPassword(
                passwordEncoder.encode(password)
        );

        customer.setPhone(phone);

        customerRepository.save(customer);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        email = email.trim().toLowerCase();

        if (email.isEmpty()) {
            model.addAttribute("error", "Please enter your email.");
            return "login";
        }

        if (password == null || password.isEmpty()) {
            model.addAttribute("error", "Please enter your password.");
            return "login";
        }

        Customer customer =
                customerRepository
                        .findByEmail(email)
                        .orElse(null);

        if (customer != null) {

            String storedPassword =
                    customer.getPassword();

            boolean validPassword = false;

            if (storedPassword != null) {

                if (storedPassword.startsWith("$2a$")
                        || storedPassword.startsWith("$2b$")
                        || storedPassword.startsWith("$2y$")) {

                    validPassword =
                            passwordEncoder.matches(
                                    password,
                                    storedPassword
                            );

                } else if (storedPassword.equals(password)) {

                    validPassword = true;

                    customer.setPassword(
                            passwordEncoder.encode(password)
                    );

                    customerRepository.save(customer);
                }
            }

            if (validPassword) {

                session.setAttribute(
                        "customerId",
                        customer.getId()
                );

                session.setAttribute(
                        "customerName",
                        customer.getName()
                );

                return "redirect:/";
            }
        }

        model.addAttribute(
                "error",
                "Invalid email or password."
        );

        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/";
    }
}

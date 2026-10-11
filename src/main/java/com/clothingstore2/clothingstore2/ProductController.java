package com.clothingstore2.clothingstore2;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/products")
    public String products(Model model) {
        List<Product> products = productRepository.findAll();

        model.addAttribute("products", products);
        model.addAttribute("category", "All Products");

        return "products";
    }

    @GetMapping("/men")
    public String men(Model model) {
        model.addAttribute("products",
                productRepository.findByCategoryIgnoreCase("Men"));
        model.addAttribute("category", "Men");

        return "category";
    }

    @GetMapping("/women")
    public String women(Model model) {
        model.addAttribute("products",
                productRepository.findByCategoryIgnoreCase("Women"));
        model.addAttribute("category", "Women");

        return "category";
    }

    @GetMapping("/kids")
    public String kids(Model model) {
        model.addAttribute("products",
                productRepository.findByCategoryIgnoreCase("Kids"));
        model.addAttribute("category", "Kids");

        return "category";
    }

    @GetMapping("/product-details")
    public String productDetails(
            @RequestParam Long product,
            Model model) {

        Product selectedProduct =
                productRepository.findById(product)
                        .orElse(null);

        model.addAttribute("product", selectedProduct);

        return "product-details";
    }

    @GetMapping("/search-results")
    public String searchResults(
            @RequestParam String q,
            Model model) {

        List<Product> products =
                productRepository.findByNameContainingIgnoreCase(q.trim());

        model.addAttribute("products", products);
        model.addAttribute("search", q);

        return "search-results";
    }
}
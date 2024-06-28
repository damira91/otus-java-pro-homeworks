package ru.otus.kudaiberdieva.homework10.application.services;

import ru.otus.kudaiberdieva.homework10.application.entity.Product;
import ru.otus.kudaiberdieva.homework10.application.repository.ProductRepository;


public class ShopServiceImpl implements ShopService {
    private final ProductRepository productRepository;
    private final IOService ioService;
    private final Cart cart;

    public ShopServiceImpl(ProductRepository productRepository, IOService ioService, Cart cart) {
        this.productRepository = productRepository;
        this.ioService = ioService;
        this.cart = cart;
    }

    @Override
    public void startShopping() {
        while (true) {
            ioService.print("Enter command (view, add, remove, exit): ");
            String command = ioService.read();

            switch (command) {
                case "exit":
                    return;
                case "view":
                    viewCart();
                    break;
                case "add":
                    ioService.print("Enter product ID to add: ");
                    Long productId = ioService.readLong();
                    Product productToAdd = productRepository.getProductById(productId);
                    if (productToAdd != null) {
                        addProductToCart(productToAdd);
                        ioService.print("Added product. Cart contents: " + cart.getProductsInCart());
                    } else {
                        ioService.print("Product with ID " + productId + " not found.");
                    }
                    break;
                case "remove":
                    ioService.print("Enter product ID to remove: ");
                    Long productIdToRemove = ioService.readLong(); // Read productId each time
                    Product productToRemove = productRepository.getProductById(productIdToRemove);
                    if (productToRemove != null) {
                        removeProductFromCart(productToRemove);
                        ioService.print("Removed product. Cart contents: " + cart.getProductsInCart());
                    } else {
                        ioService.print("Product with ID " + productIdToRemove + " not found.");
                    }
                    break;
                default:
                    ioService.print("Unknown command. Please try again.");
                    break;
            }
        }
    }

    private void addProductToCart(Product product) {
        cart.addProduct(product);
    }

    private void removeProductFromCart(Product product) {
        cart.removeProduct(product);
    }

    private void viewCart() {
        ioService.print("Products in cart:");
        ioService.print(cart.getProductsInCart().toString());
    }
}

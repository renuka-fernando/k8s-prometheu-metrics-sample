package org.renuka.k8s.prometheus.app.controllers;

import io.prometheus.client.Counter;
import org.renuka.k8s.prometheus.app.dto.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ProductController {
    // metrics: total request counter
    private static final Counter reqCount = Counter.build().name("products_http_requests_total")
            .labelNames("method", "resource", "priority").help("products: total http requests").register();

    static {
        // increase request count for exposing metric name to prometheus
        reqCount.labels("", "", "").inc();
    }

    // products
    private static Map<Integer, Product> products = new HashMap<Integer, Product>();
    private static Integer lastProductId = 105;

    // load products
    static {
        products.put(101, new Product(101, "Apples", "Food", "$1.49"));
        products.put(102, new Product(102, "Macaroni & Cheese", "Food", "$7.69"));
        products.put(103, new Product(102, "ABC Smart TV", "Electronics", "$399.99"));
        products.put(104, new Product(104, "Motor Oil", "Automobile", "$22.88"));
        products.put(105, new Product(105, "Floral Sleeveless Blouse", "Clothing", "$21.50"));
    }

    @GetMapping("/products")
    public ResponseEntity<Collection<Product>> getProducts() {
        reqCount.labels("GET", "/products", "HIGH").inc();
        return ResponseEntity.ok(products.values());
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProducts(@PathVariable("productId") Integer productId) {
        reqCount.labels("GET", "/products/$id", "HIGH").inc();

        Product product = products.get(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(product);
    }

    @GetMapping("/products/count")
    public ResponseEntity<Product.Count> getProductsCount() {
        reqCount.labels("GET", "/products/count", "LOW").inc();
        return ResponseEntity.ok(new Product.Count(products.size()));
    }

    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) throws URISyntaxException {
        reqCount.labels("POST", "/products", "HIGH").inc();

        Integer productId = ++lastProductId;
        product.setProductId(productId);
        products.put(productId, product);
        return ResponseEntity.created(new URI("/products/" + productId)).body(product);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable("productId") Integer productId,
                                                 @RequestBody Product product) throws URISyntaxException {
        reqCount.labels("PUT", "/products/$id", "LOW").inc();

        Product productOld = products.get(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        product.setProductId(productId);
        products.replace(productId, product);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("productId") Integer productId) {
        reqCount.labels("DELETE", "/products/$id", "LOW").inc();

        Product product = products.get(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        products.remove(productId);
        return ResponseEntity.ok().build();
    }
}

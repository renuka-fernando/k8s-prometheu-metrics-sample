package org.renuka.k8s.prometheus.order.app.controllers;

import io.prometheus.client.Counter;
import io.prometheus.client.Summary;
import org.renuka.k8s.prometheus.order.app.dto.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class OrderController {
    // metrics: total request counter
    private static final Counter requestCount = Counter.build().name("backend_http_requests_total")
            .labelNames("http_method", "http_url", "priority").help("Orders: Total http requests").register();
    private static final Summary requestLatency = Summary.build().name("backend_requests_latency_seconds")
            .labelNames("http_method", "http_url", "priority").help("Orders: Request latency in seconds.").register();

    static {
        // expose metrics names
        requestCount.labels("", "", "").inc(0);
        requestLatency.labels("", "", "").observe(0D);
    }

    // orders
    private static Map<Integer, Order> orders = new HashMap<>();
    private static Integer lastOrderId = 105;

    // load orders
    static {
        orders.put(11, new Order(11, 101, 2.0, new Date(1601724829373L), "placed", true));
        orders.put(12, new Order(12, 101, 1.0, new Date(1601724910492L), "placed", false));
        orders.put(13, new Order(13, 102, 5.0, new Date(1601714829373L), "delivered", true));
        orders.put(14, new Order(14, 102, 1.0, new Date(1601424829373L), "delivered", false));
        orders.put(15, new Order(15, 102, 2.0, new Date(1601774829373L), "placed", true));
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<Order> getOrders(@PathVariable("orderId") Integer orderId) {
        requestCount.labels("GET", "/orders/$id", "HIGH").inc();

        Order order = orders.get(orderId);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(order);
    }

    @GetMapping("/orders")
    public ResponseEntity<Collection<Order>> getOrders(
            @RequestParam(required = false) Integer productId,
            @RequestParam(required = false) Boolean completed
    ) {
        requestCount.labels("GET", "/orders", "HIGH").inc();
        Summary.Timer requestTimer = requestLatency.labels("GET", "/orders", "HIGH").startTimer();

        ResponseEntity<Collection<Order>> response;
        try {
            Stream<Order> productStream = orders.values().stream();
            if (productId != null) {
                productStream = productStream.filter(orders -> orders.getProductId().equals(productId));
            }
            if (completed != null) {
                productStream = productStream.filter(orders -> orders.getCompleted().equals(completed));
            }
            response = ResponseEntity.ok(productStream.collect(Collectors.toList()));
        } finally {
            requestTimer.observeDuration();
        }

        return response;
    }

    @PostMapping("/orders")
    public ResponseEntity<Order> addOrder(@RequestBody Order order) throws URISyntaxException {
        requestCount.labels("POST", "/orders", "HIGH").inc();

        Integer orderId = ++lastOrderId;
        order.setOrderId(orderId);
        orders.put(orderId, order);
        return ResponseEntity.created(new URI("/orders/" + orderId)).body(order);
    }

    @PutMapping("/orders/{orderId}")
    public ResponseEntity<Order> updateOrder(@PathVariable("orderId") Integer orderId,
                                             @RequestBody Order order) {
        requestCount.labels("PUT", "/orders/$id", "LOW").inc();

        Order orderOld = orders.get(orderId);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        order.setOrderId(orderId);
        orders.replace(orderId, order);
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<Order> deleteOrder(@PathVariable("orderId") Integer orderId) {
        requestCount.labels("DELETE", "/orders/$id", "LOW").inc();

        Order order = orders.get(orderId);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        orders.remove(orderId);
        return ResponseEntity.ok().build();
    }
}

package org.renuka.k8s.prometheus.app.controllers;

import io.prometheus.client.Counter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    static final Counter reqCount = Counter.build().name("hello_http_requests_total")
            .labelNames("method", "priority").help("hello: total http requests").register();

    @GetMapping("/hello")
    public String hello() {
        reqCount.labels("GET", "LOW").inc();
        return "Hello k8s!!!";
    }

    @GetMapping("/hello/{name}")
    public String hello(@PathVariable("name") String name) {
        reqCount.labels("GET", "HIGH").inc();
        return "Hello " + name + "!!!";
    }

    @PostMapping("/hello")
    public String hello(@RequestParam(required = false, defaultValue = "1") Integer count) {
        reqCount.labels("POST", "HIGH").inc(count);
        return "Hello k8s, incremented POST request counts with: " + count;
    }
}

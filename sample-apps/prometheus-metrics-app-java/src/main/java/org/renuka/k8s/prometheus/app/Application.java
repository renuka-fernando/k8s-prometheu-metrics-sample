package org.renuka.k8s.prometheus.app;

import io.prometheus.client.exporter.MetricsServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ServletRegistrationBean<MetricsServlet> servletRegistrationBean() {
        return new ServletRegistrationBean<>(new MetricsServlet(), "/metrics");
    }
}
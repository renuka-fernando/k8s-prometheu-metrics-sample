package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.prometheus.client.Counter;
import io.prometheus.client.exporter.MetricsServlet;

@SpringBootApplication
@RestController
public class Application {
    static final Counter requests = Counter.build()
            .name("http_requests").help("Total requests.").register();

    @RequestMapping("/hello")
    public String hello() {
        requests.inc();
        return "Hello K8S";
    }

    @RequestMapping("/hello/{name}")
    public String hello(@PathVariable("name") String name) {
        requests.inc();
        return "Hello " + name;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(new MetricsServlet(), "/metrics");
    }
}
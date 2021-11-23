package io.github.lasyard.spring.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
@ComponentScan(basePackages = {"io.github.lasyard.spring.boot"})
public class SpringBootWeb {
    @Autowired
    private Config config;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWeb.class, args);
    }

    @RequestMapping("/")
    public String home() {
        return "Hello, I am spring boot web. test.string = " + config.getString() + ".";
    }
}

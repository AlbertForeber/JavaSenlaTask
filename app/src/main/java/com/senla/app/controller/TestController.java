package com.senla.app.controller;

import com.senla.app.utils.Colors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    public TestController() {
        System.out.println(Colors.YELLOW + "BUILDED" + Colors.RESET);
    }

    @GetMapping("/")
    public String greet() {
        return "hello";
    }
}

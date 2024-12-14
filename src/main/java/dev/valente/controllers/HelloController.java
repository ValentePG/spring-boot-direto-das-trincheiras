package dev.valente.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping("hi")
    public ResponseEntity<String> hello(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return ResponseEntity.ok("hello da uri: " + uri);
    }
}

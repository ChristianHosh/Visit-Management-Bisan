package com.example.vm.rest;


import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @GetMapping("/")
    private String sayHello(){
        return "Hello World";
    }
}

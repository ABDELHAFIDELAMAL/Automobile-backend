package com.example.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1")
public class testController {

    @GetMapping(path = "/{name}")
    public String sayHello(@PathVariable String name){
        return "<h1>Hello :" + name + "</h1>" ;
    }
}

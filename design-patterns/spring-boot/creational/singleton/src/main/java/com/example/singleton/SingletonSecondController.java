package com.example.singleton;

import com.example.singleton.repositories.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SingletonSecondController {

    @Autowired Data data;

    @GetMapping("/second/echo")
    public String echo() {
        System.out.println(data);
        return "echo " + data.getData();
    }

    @GetMapping("/second/print")
    public String print() {
        System.out.println(data);
        return "print " + data.getData();
    }
}

package com.jiahui.demo.controller;

import com.jiahui.demo.component.JdSpider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {
    @Autowired
    private JdSpider jdSpider;

    @GetMapping("/doMain")
    public  String doMain(){
        jdSpider.start();
        return  "success!";

    }
}

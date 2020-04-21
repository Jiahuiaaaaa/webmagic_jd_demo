package com.jiahui.demo;

import com.jiahui.demo.component.JdSpider;
import com.jiahui.demo.controller.ItemController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

//    @Autowired
//    private ItemController itemController;
@Autowired
private JdSpider jdSpider;
    @Test
    void contextLoads() {
//        jdSpider.start();
    }

}

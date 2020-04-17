package com.jiahui.demo.service;

import com.jiahui.demo.pojo.Item;


public interface ItemService {

    int save(Item item);


    Item  findBySku(long sku);
}

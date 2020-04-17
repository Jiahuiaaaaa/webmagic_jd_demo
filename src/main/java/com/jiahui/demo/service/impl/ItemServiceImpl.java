package com.jiahui.demo.service.impl;

import com.jiahui.demo.dao.ItemDao;
import com.jiahui.demo.pojo.Item;
import com.jiahui.demo.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemDao itemDao;
    @Override
    public int save(Item item) {
        return itemDao.save(item);
    }

    @Override
    public Item findBySku(long sku) {
        return itemDao.findBySku(sku);
    }
}

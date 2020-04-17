package com.jiahui.demo.component;


import com.jiahui.demo.dao.ItemDao;
import com.jiahui.demo.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

@Component
public class JdPipeline implements Pipeline {

    @Autowired
    private ItemDao itemDao;

    @Override
    public void process(ResultItems resultItems, Task task) {

        List<Item> itemList = resultItems.get("itemList");
        //1.如果获取到列表对象，就保存每个实体类到数据库
        if (itemList!=null){
            for (Item item : itemList) {
                itemDao.save(item);
            }
        }

        Item item = resultItems.get("item");
        if(item!=null) {
            //2.如果获取到实体类，就凭借实体类sku去数据库取出对应的一条数据
            Item itemSku = itemDao.findBySku(item.getSku());
            //3.融合实体类，更新数据库
            itemSku.setPic(item.getPic());
            itemSku.setPrice(item.getPrice());
            itemSku.setTitle(item.getTitle());
            itemSku.setUrl(item.getUrl());
            itemSku.setCreateDate(item.getCreateDate());
            itemSku.setUpdateDate(item.getUpdateDate());

            itemDao.save(itemSku);
        }

    }
}

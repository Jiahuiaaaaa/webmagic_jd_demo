package com.jiahui.demo.dao;

import com.jiahui.demo.pojo.Item;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ItemDao {
    @Insert("INSERT INTO jd_item (`spu`,`sku`,`title`,`price`,`pic`,`url`,`createDate`,`updateDate`) \n" +
            "VALUES(#{spu},#{sku},#{title},#{price},#{pic},#{url},#{createDate},#{updateDate})")
    int save(Item item);

    @Select("select * from jd_item where sku = #{sku}")
    Item  findBySku(long sku);
}

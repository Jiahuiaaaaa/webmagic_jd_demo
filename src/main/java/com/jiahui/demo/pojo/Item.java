package com.jiahui.demo.pojo;

import lombok.Data;

import java.util.Date;
@Data
public class Item {
    private long id;
    private long spu;
    private long sku;
    private String title;
    private double price;
    private String pic;
    private String url;
    private Date createDate;
    private Date updateDate;





}

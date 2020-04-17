package com.jiahui.demo.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

@Component
public class JdSpider {

    @Autowired
    private JdPageProcessor pageProcessor;

    @Autowired
    private JdDownloader downloader;

    @Autowired
    private JdPipeline pipeline;

    public void start(){
        Spider.create(pageProcessor)
                .setDownloader(downloader)
                .addPipeline(pipeline)
                .addUrl("https://search.jd.com/Search?keyword=%E6%89%8B%E6%9C%BA&enc=utf-8&pvid=f50e5a02031849e4a9f8adbb9928b7ac")
                .start();
    }
}

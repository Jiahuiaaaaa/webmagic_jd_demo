package com.jiahui.demo.test;

import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author yu
 * @date 创建时间：2017年11月8日 上午11:21:34
 */
public class WebMagicTest  implements PageProcessor {

    /**
     *     部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
     */
    private Site site = Site.me()
            .setRetryTimes(3)
            .setSleepTime(100)
            //添加cookie之前一定要先设置主机地址，否则cookie信息不生效
            //.setDomain("lt.gdou.com")
            //添加抓包获取的cookie信息
            //.addCookie("xx", "xx")
            //添加请求头，有些网站会根据请求头判断该请求是由浏览器发起还是由爬虫发起的
            .addHeader("User-Agent",
                    "ozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36 Core/1.47.516.400 QQBrowser/9.4.8188.400")
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
            .addHeader("Accept-Encoding", "gzip, deflate, sdch").addHeader("Accept-Language", "zh-CN,zh;q=0.8")
            .addHeader("Connection", "keep-alive");

    //接口方法的实现，返回我们配置的site
    @Override
    public Site getSite() {
        return site;
    }

    //PageProcessor最重要的部分，需要我们根据具体的页面进行定制
    @Override
    public void process(Page page) {

        //直接从浏览器获取的xpath信息
//      page.getHtml().xpath("//*[@id=\"article_list\"]/div[2]/div[1]/h1/span/a");
        //不写[2]时代表所有的标题，所有修改删去[2],使用all()方法获得全部的标题此时返回的是List<String>对象
        List<String> list = page.getHtml().xpath("//*[@id=\"article_list\"]/div/div[1]/h1/span/a/text()").all();
        for(String name : list){
            System.out.println(name);
        }
    }

    public static void main(String[] args) {
        //加入待爬去的衔接
        String[] urls = {"http://blog.csdn.net/iceyung"};
        Spider.create(new WebMagicTest())
                .addUrl(urls)
                //开启5个线程抓取
                .thread(1)
                //启动爬虫
                .run();
        System.out.println("爬取结束");
    }

}

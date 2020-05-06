package com.jiahui.demo.component;


import com.jiahui.demo.pojo.Item;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Component
public class JdPageProcessor implements PageProcessor {
    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        //1.判断是列表页面还是详情页面
        List<Selectable> nodes = html.css("ul.gl-warp > li.gl-item").nodes();
        if (nodes.size() > 0) {
            //2.如果是列表页面就获取所有的地址传给队列
            List<String> hrefs = html.css("div.gl-i-wrap div.p-img a", "href").all();
            page.addTargetRequests(hrefs);
            //4.获取列表中每个商品的sku和spu封装成实体类集合传给pipeline(因为详情页面不好找；通过详情页面的sku即可找到对应的spu)
            List<Item> itemList = new ArrayList<>();
            Document document = html.getDocument();
            Elements elements = document.select("ul.gl-warp.clearfix li.gl-item");
            for (Element element : elements) {
                String sku = element.attr("data-sku");
                String spu = element.attr("data-spu");
                Item item = new Item();
                item.setSku(Long.parseLong(sku));
                item.setSpu(Long.parseLong(spu));
                itemList.add(item);
            }
            page.putField("itemList", itemList);
            //3.传给队列一个下一页地址：http://www.nextPage.com并添加附件，内容为这一页地址，
            //     方便downloader对象点击下一页按钮，为了以放队列删除相同地址，添加一个 ?url=当前页地址
            Request request = new Request("http://nextpage?url=" + page.getUrl());
            Map<String, Object> map = new HashMap<>();
            map.put("currentPageUrl", page.getUrl().get());
            request.setExtras(map);
            page.addTargetRequest(request);
        } else {
            //5.如果是详情页面就封装成实体类传入pipeline

            //库存量单位（最小品类单元）
            Long sku = null;
            try {
                sku = Long.parseLong(html.css("div.preview-info div.left-btns a.follow.J-follow", "data-id").get());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            //商品标题
            String title = html.css(".sku-name", "text").get();
            //商品价格
            Double price = Double.parseDouble(html.css("div.dd span.p-price span.price", "text").get());
            //商品图片
            String picture = parsePicture(page);
            //商品详情地址
            String url = page.getUrl().get();
            //创建时间
            Date createDate = new Date();
            //更新时间
            Date updateDate = new Date();

            Item item = new Item();
            item.setSku(sku);
            item.setUpdateDate(updateDate);
            item.setCreateDate(createDate);
            item.setUrl(url);
            item.setTitle(title);
            item.setPrice(price);
            item.setPic(picture);
            page.putField("item", item);

        }
    }

    @Override
    public Site getSite() {
        return Site.me()
                //必须设置这个请求头，不然拿不到数据
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:73.0) Gecko/20100101 Firefox/73.0");
    }


    /**
     * 保存图片到本地并返回本地图片地址
     *
     * @param page
     * @return
     */
    public String parsePicture(Page page) {
        String src = "http:" + page.getHtml().css("div#spec-n1.jqzoom.main-img img#spec-img", "src").get();
        PoolingHttpClientConnectionManager hc = new PoolingHttpClientConnectionManager();
        CloseableHttpClient client = HttpClients.custom()
                .setConnectionManager(hc)
                .build();
        HttpGet get = new HttpGet(src);
        get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:73.0) Gecko/20100101 Firefox/73.0");
        String fileName = UUID.randomUUID() + src.substring(src.lastIndexOf("."));
        String path = "D:\\webmagic\\temp\\img\\";
        String finalPath = path + fileName;
        try {
            CloseableHttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            entity.writeTo(new FileOutputStream(finalPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return finalPath;
    }
}
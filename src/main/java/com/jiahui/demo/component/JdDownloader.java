package com.jiahui.demo.component;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.PlainText;


import java.util.List;


@Component
public class JdDownloader implements Downloader {

    private RemoteWebDriver chromeDriver;

    public JdDownloader(){
        //配置参数
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        //        设置为 headless 模式 （必须）
        //          chromeOptions.addArguments("--headless");
        // 设置浏览器窗口打开大小  （非必须）
        chromeOptions.addArguments("--window-size=1024,768");
        chromeDriver = new ChromeDriver(chromeOptions);
//        chromeDriver.manage().timeouts().setScriptTimeout(3, TimeUnit.SECONDS);
    }


//    We were seeing something similar with Chrome and the issue came down to the way we were maximizing the browser before
//    running the tests.
//    We switched from this:
//            Driver.Manage().Window.Maximize();
//
//    To this (for Chrome only):
//            if (typeof(TWebDriver) == typeof(ChromeDriver))
//            { var options = new ChromeOptions(); options.AddArgument("start-maximized");
//            driver = new ChromeDriver(driverPath, options);}

    @Override
    public Page download(Request request, Task task) {
        //1.判断获取到的地址是下一页地址还是普通地址(http://nextpage?url=)
        if (request.getUrl().contains("nextpage")) {
            //2.若是下一页地址则获取附件，用无头浏览模式加载到该地址
            String currentPageUrl = (String) request.getExtra("currentPageUrl");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            chromeDriver.get(currentPageUrl);
            //3.再用Selenium操作浏览器点击下一页，并休眠3秒钟
            chromeDriver.findElementByCssSelector("div#J_topPage.f-pager a.fp-next").click();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //4.用Selenium操作浏览器拉到最下方
            chromeDriver.executeScript("window.scrollTo(0, document.body.scrollHeight - 300)");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //取渲染之后的页面
            String pageSource = chromeDriver.getPageSource();
            //5.吧渲染好的页面传给pageProcessor
            return createPage(pageSource,chromeDriver.getCurrentUrl());
        } else {
            //6.若是普通地址，也要区分是第一页地址还是详情页面地址
            chromeDriver.get(request.getUrl());
            List<WebElement> elements = chromeDriver.findElementsByCssSelector(".gl-item");
            if (elements.size()>0) {
                //8.若是第一页（有列表的页面则是第一页），用Selenium操作浏览器拉到最下方
                chromeDriver.executeScript("window.scrollTo(0, document.body.scrollHeight - 300)");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //9.传输渲染好的页面传给pageProcessor
            }else {
                //7.若是详情页面则直接传给pageProcessor
            }
            return createPage(chromeDriver.getPageSource(),chromeDriver.getCurrentUrl());
        }
    }

    public Page createPage(String pageSource,String url){
        Page page = new Page();
        //封装page对象
        page.setRawText(pageSource);
        page.setUrl(new PlainText(url));
        //设置request对象(必要)
        page.setRequest(new Request(url));
        //设置页面抓取成功(必要)
        page.setDownloadSuccess(true);
        return page;
    }

    @Override
    public void setThread(int i) {

    }
}
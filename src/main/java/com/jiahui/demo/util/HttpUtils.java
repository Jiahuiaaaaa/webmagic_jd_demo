package com.jiahui.demo.util;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
/**
 * 创建实例
 * @author ELIAN-FM-JSJ297
 */
@Component
public class HttpUtils {
    /**
     * 使用连接池
     */
    private PoolingHttpClientConnectionManager cm;

    /**
     * 需要声明构造方法，因为参数不需要从外面传进来，所以不需要参数
     * 为什么需要构造方法，是因为声明的这个连接池需要赋于属性的值
     */
    public HttpUtils() {
        this.cm = new PoolingHttpClientConnectionManager();
        //设置最大连接数
        this.cm.setMaxTotal(100);
        //设置每个主机的最大连接数
        this.cm.setDefaultMaxPerRoute(10);
    }


    //这里使用get请求获取页面数据，返回类型是string字符串类型

    /**
     * 根据请求地址下载页面数据
     * @param url
     * @return
     */
    public String doGetHTML(String url){
        //获取HttpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        //创建httpGet对象，设置url地址
        HttpGet httpGet=new HttpGet(url);
        //设置请求信息
        httpGet.setConfig(this.getConfig());


        CloseableHttpResponse response=null;

        try {
            //使用httpClient发起请求，获取响应
            response=httpClient.execute(httpGet);
            //解析响应，返回结果
            if(response.getStatusLine().getStatusCode()==200){
                //判断响应体Entity是否为空，如果不为空就可以使用HttpUtils
                if(response.getEntity()!=null){
                    String content = EntityUtils.toString(response.getEntity(), "utf8");

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关闭response
            if(response!=null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return "";
    }

    /**
     * 设置请求的信息
     * @return
     */
    private RequestConfig getConfig() {
        RequestConfig config=RequestConfig.custom()
                //创建连接的最长时间
                .setConnectTimeout(1000)
                //获取连接的最长时间
                .setConnectionRequestTimeout(500)
                //数据传输的最长时间
                .setSocketTimeout(500)
                .build();
        return config;
    }

    /**
     * 下载图片
     * @param url
     * @return
     */
    public String doGetImage(String url){
        //获取HttpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        //创建httpGet对象，设置url地址
        HttpGet httpGet=new HttpGet(url);
        //设置请求信息
        httpGet.setConfig(this.getConfig());


        CloseableHttpResponse response=null;

        try {
            //使用httpClient发起请求，获取响应
            response=httpClient.execute(httpGet);
            //解析响应，返回结果
            if(response.getStatusLine().getStatusCode()==200){
                //判断响应体Entity是否为空，如果不为空就可以使用HttpUtils
                if(response.getEntity()!=null){
                    //下载图片
                    //获取图片的后缀
                    String extName=url.substring(url.lastIndexOf("."));
                    //创建图片名，重命名图片
                    String picName= UUID.randomUUID().toString()+extName;
                    //下载图片
                    //声明OutputStream
                    OutputStream outputStream=new FileOutputStream(new File("D:\\suibian\\image")+picName);
                    response.getEntity().writeTo(outputStream);
                    //图片下载完成，返回图片名称
                    return picName;

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关闭response
            if(response!=null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return "";
    }
}

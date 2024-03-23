package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    public static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";


    private String getOpenid(String code) {
        Map<String, String > map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String result = HttpClientUtil.doGet(WX_LOGIN_URL, map);
        //转为json格式
        JSONObject jsonObject = JSON.parseObject(result);
        String openid = jsonObject.getString("openid");
        if (openid == null) {
            throw new LoginFailedException("未返回openid，登录失败");
        }
        return openid;

//        //创建Httpclient对象
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        String result = "";
//        CloseableHttpResponse response = null;
//
//        try {//创建请求对象
//            URIBuilder builder = new URIBuilder(WX_LOGIN_URL);
//            builder.addParameter("appid", weChatProperties.getAppid());
//            builder.addParameter("secret", weChatProperties.getSecret());
//            builder.addParameter("js_code", code);
//            builder.addParameter("grant_type", "authorization_code");
//            URI uri = builder.build();
//
//            //创建GET请求
//            HttpGet httpGet = new HttpGet(uri);
//
//            //发送请求
//            response = httpClient.execute(httpGet);
//
//            //判断响应状态,查询返回结果
//            if (response.getStatusLine().getStatusCode() == 200) {
//                HttpEntity resJson = response.getEntity();
//                result = EntityUtils.toString(resJson);
//            } else throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                response.close();
//                httpClient.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        //转为json格式
//        JSONObject jsonObject = JSON.parseObject(result);
//        String openid = jsonObject.getString("openid");
//        if (openid == null) {
//            throw new LoginFailedException("未返回openid，登录失败");
//        }
//        return openid;

    }
    public User wxLogin(String code) {
        String openid = getOpenid(code);
        //成功接受返回的openid，判断是否为新用户
        User user = userMapper.getByOpenid(openid);
        if (user == null) {
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        return user;
    }
}

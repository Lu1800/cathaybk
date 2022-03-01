package com.example.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class HttpService {


    public String http(String url) throws JsonProcessingException {

//        String url = "https://api.coindesk.com/v1/bpi/currentprice.json";
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> submap = new HashMap<String, Object>();

        // 方式一：GET 方式獲取 JSON 串數據
        String result = restTemplate.getForObject(url, String.class);
        System.out.println("get_product1返回結果：" + result);

        return result;
    }

}

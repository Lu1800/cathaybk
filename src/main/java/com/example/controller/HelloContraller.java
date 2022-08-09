package com.example.controller;

import com.example.entity.Coin;
import com.example.repository.CoinRepository;
import com.example.service.HttpService;
import com.example.vo.ResponseVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class HelloContraller {


        @Autowired
        private CoinRepository coinRepository;

        @Autowired
        private HttpService httpService;


    @GetMapping("/Coin")
    public ResponseVO checkCoinInfo() throws JsonProcessingException, ParseException {

        String uuId = String.valueOf(UUID.randomUUID());

        return new ResponseVO(uuId,"200","Success",coinRepository.findAll());
    }


    @PostMapping("/Coin")
    public void saveCoin(@RequestBody Coin coin) throws JsonProcessingException, ParseException {

        coinRepository.save(coin);
    }


    @PutMapping("/Coin")
    public ResponseVO updateCoin(@RequestBody Coin coin) throws JsonProcessingException, ParseException {

        String uuId = String.valueOf(UUID.randomUUID());
        coinRepository.save(coin);

        return new ResponseVO(uuId,"200","Success",coinRepository.getById(coin.getCode()));

    }


    @DeleteMapping("/Coin")
    public void deleteCoin(@RequestBody Coin coin) throws JsonProcessingException, ParseException {

        coinRepository.delete(coin);
    }


    @GetMapping("/coindesk")
    public ResponseVO getBPI() throws JsonProcessingException, ParseException {

        String uuId = String.valueOf(UUID.randomUUID());
        String response = httpService.http("https://api.coindesk.com/v1/bpi/currentprice.json");

        JSONObject respJson = new JSONObject(response);

        return new ResponseVO(uuId,"200","Success",respJson.toMap());
    }


    @RequestMapping("/coindeskInfoConvert")
    public ResponseVO coindeskInfoConvert() throws JsonProcessingException, ParseException {

        String uuId = String.valueOf(UUID.randomUUID());

        String response = httpService.http("https://api.coindesk.com/v1/bpi/currentprice.json");
        System.out.println(response);

        JSONObject respJson = new JSONObject(response);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
        JSONObject output = new JSONObject();

        SimpleDateFormat sdf02 = new SimpleDateFormat("MMM d, yyyy HH:mm:ss 'UTC'", Locale.ENGLISH);
        sdf02.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = sdf02.parse(respJson.getJSONObject("time").getString("updated"));
        output.put("updated",sdf.format(date));

        JSONObject usdJson = respJson.getJSONObject("bpi").getJSONObject("USD");
        JSONObject gbpJson = respJson.getJSONObject("bpi").getJSONObject("GBP");
        JSONObject eurJson = respJson.getJSONObject("bpi").getJSONObject("EUR");

        Coin usd = coinRepository.getById(usdJson.getString("code"));
        Coin gbp = coinRepository.getById(gbpJson.getString("code"));
        Coin eur = coinRepository.getById(eurJson.getString("code"));

        usdJson.remove("symbol");
        usdJson.remove("rate_float");
        usdJson.put("description",usd.getTranslate());

        gbpJson.remove("symbol");
        gbpJson.remove("rate_float");
        gbpJson.put("description",gbp.getTranslate());

        eurJson.remove("symbol");
        eurJson.remove("rate_float");
        eurJson.put("description",eur.getTranslate());

        output.put("Bitcoin Price Index",new JSONObject().put("USD",usdJson ).put("GBP",gbpJson).put("EUR",eurJson));

        return new ResponseVO(uuId,"200","Success",output.toMap());

    }
}

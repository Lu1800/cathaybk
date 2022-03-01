package com.example.controller;

import com.example.entity.Coin;
import com.example.repository.CoinRepository;
import com.example.service.HttpService;
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

    @ResponseBody
    @GetMapping("/Coin")
    public List<Coin> checkCoinInfo() throws JsonProcessingException, ParseException {

        return coinRepository.findAll();
    }

    @ResponseBody
    @PostMapping("/Coin")
    public void saveCoin(Coin coin) throws JsonProcessingException, ParseException {

        coinRepository.save(coin);
    }

    @ResponseBody
    @PutMapping("/Coin")
    public Coin updateCoin(Coin coin) throws JsonProcessingException, ParseException {

        coinRepository.save(coin);
        return coinRepository.getById(coin.getCode());
    }

    @ResponseBody
    @DeleteMapping("/Coin")
    public void deleteCoin(Coin coin) throws JsonProcessingException, ParseException {

        coinRepository.delete(coin);
    }

    @ResponseBody
    @GetMapping("/coindesk")
    public String getBPI(Coin coin) throws JsonProcessingException, ParseException {

        String response = httpService.http("https://api.coindesk.com/v1/bpi/currentprice.json");

        return response;
    }

    @ResponseBody
    @RequestMapping("/coindeskInfoConvert")
    public String coindeskInfoConvert() throws JsonProcessingException, ParseException {

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


        return output.toString();

    }
}

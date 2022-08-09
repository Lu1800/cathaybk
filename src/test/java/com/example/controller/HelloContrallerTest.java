package com.example.controller;

import com.example.DemoApplication;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.jayway.jsonpath.JsonPath;


import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = DemoApplication.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HelloContrallerTest {

    @Autowired
    private MockMvc mockMvc;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Test
    @Order(1)
    void checkCoinInfo() throws Exception {

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/Coin"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(Charset.defaultCharset());

//        System.out.println(result);
        JSONObject jsonObject = new JSONObject(result);
        Assertions.assertThat(String.valueOf(jsonObject.getJSONArray("data"))).isEqualTo("[{\"code\":\"USD\",\"translate\":\"美金\"},{\"code\":\"GBP\",\"translate\":\"英鎊\"},{\"code\":\"EUR\",\"translate\":\"歐元\"}]");
    }

    @Test
    @Order(2)
    void saveCoin() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/Coin").contentType(APPLICATION_JSON_UTF8)
                .content("{\n" +
                        "    \"code\": \"TWD\",\n" +
                        "    \"translate\": \"台幣\"\n" +
                        "}"))
                .andExpect(status().isOk());

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/Coin"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(Charset.defaultCharset());

//        System.out.println(result);
        JSONObject jsonObject = new JSONObject(result);
        Assertions.assertThat(String.valueOf(jsonObject.getJSONArray("data")))
                .isEqualTo("[{\"code\":\"USD\",\"translate\":\"美金\"},{\"code\":\"GBP\",\"translate\":\"英鎊\"},{\"code\":\"EUR\",\"translate\":\"歐元\"},{\"code\":\"TWD\",\"translate\":\"台幣\"}]");
    }

    @Test
    @Order(3)
    void updateCoin() throws Exception {

        String result = mockMvc.perform(MockMvcRequestBuilders.put("/Coin").contentType(APPLICATION_JSON_UTF8)
                        .content("{\n" +
                                "    \"code\": \"TWD\",\n" +
                                "    \"translate\": \"臺幣\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(Charset.defaultCharset());

//        System.out.println(result);
        JSONObject jsonObject = new JSONObject(result);
        Assertions.assertThat(String.valueOf(jsonObject.getJSONObject("data")))
                .isEqualTo("{\"code\":\"TWD\",\"translate\":\"臺幣\"}");
    }

    @Test
    @Order(4)
    void deleteCoin() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/Coin").contentType(APPLICATION_JSON_UTF8)
                        .content("{\n" +
                                "    \"code\": \"TWD\",\n" +
                                "    \"translate\": \"臺幣\"\n" +
                                "}"))
        .andExpect(status().isOk());

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/Coin"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(Charset.defaultCharset());

//        System.out.println(result);
        JSONObject jsonObject = new JSONObject(result);
        Assertions.assertThat(String.valueOf(jsonObject.getJSONArray("data"))).isEqualTo("[{\"code\":\"USD\",\"translate\":\"美金\"},{\"code\":\"GBP\",\"translate\":\"英鎊\"},{\"code\":\"EUR\",\"translate\":\"歐元\"}]");
    }

    @Test
    @Order(5)
    void getBPI() throws Exception {

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/coindesk"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(Charset.defaultCharset());

//        System.out.println(result);
        JSONObject jsonObject = new JSONObject(result);
        Assertions.assertThat(jsonObject.getJSONObject("data").has("bpi")).isTrue();
    }

    @Test
    @Order(6)
    void coindeskInfoConvert() throws Exception {

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/coindeskInfoConvert"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(Charset.defaultCharset());

//        System.out.println(result);
        JSONObject jsonObject = new JSONObject(result);
        Assertions.assertThat(jsonObject.getJSONObject("data").has("Bitcoin Price Index")).isTrue();
    }
}
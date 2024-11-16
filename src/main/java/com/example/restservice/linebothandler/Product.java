package com.example.restservice.linebothandler;

import jdk.jfr.DataAmount;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Product {
    private String productId;
    private String productUrl;
    private String productName;
    private Integer productPrice;
    private Integer productPriceInTwd;
    private Map<String, List<String>> stockStatus;
}

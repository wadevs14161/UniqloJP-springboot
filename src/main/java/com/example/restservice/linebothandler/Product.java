package com.example.restservice.linebothandler;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class Product {
    private String productId;
    private String productUrl;
    private Integer productPrice;
    private Integer productPriceInTwd;
    private ArrayList<String> communicationCodeList;
    private ArrayList<String> colorIDList;
    private Map<String, List<String>> stockStatus;

    public Product() {
        this.productPrice = 0;
        this.productPriceInTwd = 0;
        this.communicationCodeList = new ArrayList<>();
        this.colorIDList = new ArrayList<>();
    }

    public void addCommunicationCode(String code) {
        communicationCodeList.add(code);
    }

    public void addColorID(String ID) {
        colorIDList.add(ID);
    }
}

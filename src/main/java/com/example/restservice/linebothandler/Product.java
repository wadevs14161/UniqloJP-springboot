package com.example.restservice.linebothandler;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Product {
    private String productId;
    private String productUrl;
    private Integer productPrice;
    private Integer productPriceInTwd;

    /* example ["08157736", "08157737", "08157738"] */
    private ArrayList<String> l2IdList;
    /* example Key: 08157736, Value: 9 */
    private Map<String, String> l2IdToColorCodeMap;
    /* example Key: 08157736, Value: 4 */
    private Map<String, String> l2IdToSizeCodeMap;
    /* example Key: 黑, Value: ["S", "M", "XL"] */
    private Map<String, List<String>> stockStatus;

    public Product() {
        this.productPrice = 0;
        this.productPriceInTwd = 0;
        this.l2IdList = new ArrayList<>();
        this.l2IdToColorCodeMap = new HashMap<>();
        this.l2IdToSizeCodeMap = new HashMap<>();
        this.stockStatus = new HashMap<>();
    }

    public void addl2IdList(String code) {
        l2IdList.add(code);
    }

    public void addl2IdToColorCodeMap(String id, String colorCode) {
        l2IdToColorCodeMap.put(id, colorCode);
    }

    public void addl2IdToSizeCodeMap(String id, String sizeCode) {
        l2IdToSizeCodeMap.put(id, sizeCode);
    }
}


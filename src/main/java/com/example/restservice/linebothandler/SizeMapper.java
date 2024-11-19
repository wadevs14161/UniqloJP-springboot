package com.example.restservice.linebothandler;

import java.util.HashMap;
import java.util.Map;

public class SizeMapper {
    public static Map<Integer, String> sizeMap = new HashMap<>();
    static {
        sizeMap.put(1, "XXS");
        sizeMap.put(2, "XS");
        sizeMap.put(3, "S");
        sizeMap.put(4, "M");
        sizeMap.put(5, "L");
        sizeMap.put(6, "XL");
        sizeMap.put(7, "XXL");
        sizeMap.put(8, "3XL");
        sizeMap.put(9, "4XL");
        sizeMap.put(23, "23-25");
        sizeMap.put(25, "25-27");
        sizeMap.put(27, "27-29");
        sizeMap.put(60, "60");
        sizeMap.put(70, "70");
        sizeMap.put(80, "80");
        sizeMap.put(90, "90");
        sizeMap.put(100, "100");
        sizeMap.put(110, "110");
        sizeMap.put(120, "120");
        sizeMap.put(130, "130");
        sizeMap.put(140, "140");
        sizeMap.put(150, "150");
        sizeMap.put(160, "160");
        sizeMap.put(499, "AA 65/70");
        sizeMap.put(500, "AB 65/70");
        sizeMap.put(501, "CD 65/70");
        sizeMap.put(502, "EF 65/70");
        sizeMap.put(503, "AB 75/80");
        sizeMap.put(504, "CD 75/80");
        sizeMap.put(505, "EF 75/80");
        sizeMap.put(506, "AB 85/90");
        sizeMap.put(507, "CD 85/90");
        sizeMap.put(508, "EF 85/90");
    }

    public static String getSize(int sizeCode) {
        return sizeMap.get(sizeCode);
    }
}

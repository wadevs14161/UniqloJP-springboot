package com.example.restservice.linebothandler;

import java.util.HashMap;
import java.util.Map;

public class ColorMapper {
    public static Map<Integer, String> colorRanges = new HashMap<>();
    static {
        colorRanges.put(0, "白");
        colorRanges.put(1, "象牙白");
        colorRanges.put(8, "灰");
        colorRanges.put(9, "黑");
        colorRanges.put(12, "粉紅");
        colorRanges.put(17, "紅");
        colorRanges.put(19, "酒紅");
        colorRanges.put(29, "橘");
        colorRanges.put(30, "自然");
        colorRanges.put(32, "米");
        colorRanges.put(33, "卡其");
        colorRanges.put(39, "棕");
        colorRanges.put(40, "奶油");
        colorRanges.put(49, "黃");
        colorRanges.put(55, "綠");
        colorRanges.put(59, "橄欖綠");
        colorRanges.put(68, "藍");
        colorRanges.put(69, "海軍藍");
        colorRanges.put(79, "紫");
        colorRanges.put(90, "非特定色");
    }

    public static String getColorName(int colorCode) {
        for (Map.Entry<Integer, String> entry : colorRanges.entrySet()) {
            if (colorCode <= entry.getKey()) {
                return entry.getValue();
            }
        }
        return "Others 其他";
    }
}

package com.example.restservice.linebothandler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


public class testJsoup {
    public static void main(String[] args) {
        try {
            // Connect to the Google Finance page for JPY to TWD exchange rate
            String url = "https://wise.com/gb/currency-converter/jpy-to-twd-rate";
            Document doc = Jsoup.connect(url)
                                .get();

            // Get the exchange rate with the class name "YMlKec fxKbKc"
            Element element = doc.getElementsByClass("text-success").first();
            if (element != null) {
                System.out.println(element.text());
            } else {
                System.out.println("Element not found");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

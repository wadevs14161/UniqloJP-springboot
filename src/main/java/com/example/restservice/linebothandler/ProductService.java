package com.example.restservice.linebothandler;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class ProductService {

    private String productUrlTemplate = "https://www.uniqlo.com/jp/ja/products/%s";
    private String productDetailsUrlTemplate = "https://www.uniqlo.com/jp/api/commerce/v5/ja/products?q=%s&queryRelaxationFlag=true&offset=0&limit=36&httpFailure=true";

    public String scrapeProduct(String productId){
        String productUrl = String.format(productUrlTemplate, productId);
        Product product = new Product();
        try {

            // Open url
            Connection.Response response = Jsoup.connect(productUrl)
                    .timeout(3000)
                    .execute();
            int statusCode = response.statusCode();
            if (statusCode == 200) {
                product.setProductId(productId);
                product.setProductUrl(productUrl);
            } else {
                return "商品不存在日本官網拉~";
            }

            // Get product information
            String productDetailsUrl = String.format(productDetailsUrlTemplate, productId);
            System.out.println(productDetailsUrl);
            URL url = new URL(productDetailsUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {

                // Convert content into string
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder content = new StringBuilder();
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    content.append(inputLine);
                }
                reader.close();
                String jsonString = content.toString();

                // Convert into JSON object and set values for Product
                JSONObject productInfo = new JSONObject(jsonString);
                Integer productPrice = null;
                try {
                    productPrice = Integer.valueOf(productInfo.getJSONObject("result")
                            .getJSONArray("items")
                            .getJSONObject(0)
                            .getJSONObject("prices")
                            .getJSONObject("base")
                            .getInt("value"));
                } catch (NumberFormatException e) {
                    // Handle the error, e.g., log an error message or set a default value
                    System.err.println("Error parsing product price: " + e.getMessage());
                    productPrice = 0; // Or another default value
                }
                product.setProductPrice(productPrice);

                // Convert price in JPY to TWD
                Integer productPriceTWD = null;
                final String currencyUrl = "https://wise.com/gb/currency-converter/jpy-to-twd-rate";
                Document currencyDocument = Jsoup.connect(currencyUrl)
                        .timeout(3000)
                        .get();
                // Assuming the class name remains consistent
                Element exchangeRateElement = currencyDocument.getElementsByClass("text-success").first();

                String exchangeRateString = exchangeRateElement.text();

                // Convert the string to a double
                double exchangeRate = Double.parseDouble(exchangeRateString);

                productPriceTWD = (int) (productPrice * exchangeRate);
                product.setProductPriceInTwd(productPriceTWD);

            } else {
                return "商品資訊找不到耶!";
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return "商品連結: " + product.getProductUrl() + "\n" +
                "日本售價: ¥" + product.getProductPrice().toString() + "\n" +
                "折合台幣: " + product.getProductPriceInTwd() + "元";
    }


}

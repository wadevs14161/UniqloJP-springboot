package com.example.restservice.linebothandler;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
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
import java.util.Iterator;


public class ProductService {

    private final String productUrlTemplate = "https://www.uniqlo.com/jp/ja/products/%s";
//    private String productDetailsUrlTemplate = "https://www.uniqlo.com/jp/api/commerce/v5/ja/products?q=%s&queryRelaxationFlag=true&offset=0&limit=36&httpFailure=true";
    private String productDetailsUrlTemplate = "https://www.uniqlo.com/jp/api/commerce/v5/ja/products/E%s-000/price-groups/00/l2s?withPrices=true&withStocks=true&includePreviousPrice=false&httpFailure=true";
    public String scrapeProduct(String productId){
        String productUrl = String.format(productUrlTemplate, productId);
        Product product = new Product();
        try {
            // Open url
            Connection.Response response = Jsoup.connect(productUrl)
                    .timeout(5000)
                    .execute();
            int statusCode = response.statusCode();
            if (statusCode == 200) {
                product.setProductId(productId);
                product.setProductUrl(productUrl);
            } else {
                return "商品不存在日本官網拉~";
            }

            String productDetailsUrl = String.format(productDetailsUrlTemplate, productId);
            URL url = new URL(productDetailsUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                System.out.println("Product details found in default url.");

                // Convert content into string
                JSONObject productInfo = getJsonObject(conn);

                /* Find product price in JSONObject */
                JSONObject priceObject = productInfo.getJSONObject("result").getJSONObject("prices");
                Iterator<String> keysPriceObject = priceObject.keys();
                while (keysPriceObject.hasNext()) {
                    String key = keysPriceObject.next();
                    Boolean isDualPrice = priceObject
                            .getJSONObject(key)
                            .getBoolean("isDualPrice");
                    Integer price = priceObject
                            .getJSONObject(key)
                            .getJSONObject("base")
                            .getInt("value");
                    if (!isDualPrice && price > product.getProductPrice()) {
                        product.setProductPrice(price);
                        // Set product price based on productDetailsAltUrlTemplate
                        product.setProductPrice(price);
                        break;
                    }
                }

                /* Find other info of product */
                JSONArray l2sArray = productInfo.getJSONObject("result").getJSONArray("l2s");
//                Iterator<String> keysl2sObject = priceObject.keys();
//                while (keysl2sObject.hasNext()) {
//                    String key = keysl2sObject.next();
//                    product.addCommunicationCode(l2sArray.getString("communicationCode"));
//                    product.addColorID(l2sArray.getString("l2Id"));
//                }
                for (int i = 0; i < l2sArray.length(); i++) {
                    product.addCommunicationCode(l2sArray.getJSONObject(i).getString("communicationCode"));
                    product.addColorID(l2sArray.getJSONObject(i).getString("l2Id"));
                }

                
                // Convert price in JPY to TWD
                final String currencyUrl = "https://wise.com/gb/currency-converter/jpy-to-twd-rate";
                Document currencyDocument = Jsoup.connect(currencyUrl)
                        .timeout(3000)
                        .get();

                // Assuming the class name remains consistent
                Element exchangeRateElement = currencyDocument.getElementsByClass("text-success").first();

                String exchangeRateString = exchangeRateElement.text();

                // Convert the string to a double
                double exchangeRate = Double.parseDouble(exchangeRateString);

                // product price in TWD
                product.setProductPriceInTwd((int) (product.getProductPrice() * exchangeRate));


            } else {
                return "詳細資訊找不到耶!" + "\n" + product.getProductUrl();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return "商品連結: " + product.getProductUrl() + "\n" +
                "日本售價: ¥" + product.getProductPrice().toString() + "\n" +
                "折合台幣: " + product.getProductPriceInTwd() + "元";
    }

    @NotNull
    private static JSONObject getJsonObject(HttpsURLConnection conn) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder content = new StringBuilder();
        String inputLine;
        while ((inputLine = reader.readLine()) != null) {
            content.append(inputLine);
        }
        reader.close();
        String jsonString = content.toString();

        // Convert into JSON object
        // Find product price
        return new JSONObject(jsonString);
    }


}

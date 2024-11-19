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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class ProductService {

    private final String productUrlTemplate = "https://www.uniqlo.com/jp/ja/products/%s";
//    private String productDetailsUrlTemplate = "https://www.uniqlo.com/jp/api/commerce/v5/ja/products?q=%s&queryRelaxationFlag=true&offset=0&limit=36&httpFailure=true";
    private String productDetailsUrlTemplate = "https://www.uniqlo.com/jp/api/commerce/v5/ja/products/E%s-000/price-groups/00/l2s?withPrices=true&withStocks=true&includePreviousPrice=false&httpFailure=true";
    public String scrapeProduct(String productId) {
        String productUrl = String.format(productUrlTemplate, productId);
        Product product = new Product();
        StringBuilder stockStatusString = new StringBuilder();
        try {
            // Open url
            HttpsURLConnection connection = (HttpsURLConnection) new URL(productUrl).openConnection();
            if (connection.getResponseCode() == 200) {
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

                /* Find communicationCode, colorCode of product */
                JSONArray l2sArray = productInfo.getJSONObject("result").getJSONArray("l2s");

                for (int i = 0; i < l2sArray.length(); i++) {
                    // Get l2Id and append to list
                    String l2Id = l2sArray.getJSONObject(i).getString("l2Id");
                    product.addl2IdList(l2Id);

                    // Add (l2Id, colorCode) to hashmap
                    String colorCode = l2sArray
                            .getJSONObject(i)
                            .getJSONObject("color")
                            .getString("displayCode");
                    product.addl2IdToColorCodeMap(l2Id, colorCode);

                    // Add (l2Id, sizeCode) to hashmap
                    String sizeCode = l2sArray
                            .getJSONObject(i)
                            .getJSONObject("size")
                            .getString("displayCode");
                    product.addl2IdToSizeCodeMap(l2Id, sizeCode);
                }

                JSONObject stockObject = productInfo.getJSONObject("result").getJSONObject("stocks");
                for (int i = 0; i < product.getL2IdList().size(); i++) {
                    String l2Id = product.getL2IdList().get(i);
                    String stockStatusCode = stockObject.getJSONObject(l2Id).getString("statusCode");
                    String outOfStock = "STOCK_OUT";
                    if (!stockStatusCode.matches(outOfStock)) {
                        // Map l2Id to colorCode and map colorCode to color
                        Integer colorCode = Integer.valueOf(product.getL2IdToColorCodeMap().get(l2Id));
                        String color = ColorMapper.getColorName(colorCode);
                        Integer sizeCode = Integer.valueOf(product.getL2IdToSizeCodeMap().get(l2Id));
                        String size = SizeMapper.getSize(sizeCode);

                        // Check if the color exists in the stockStatus map
                        if (product.getStockStatus().containsKey(color)) {
                            // If it exists, append the size to the existing value list
                            List<String> sizeList = product.getStockStatus().get(color);
                            sizeList.add(size);
                        } else {
                            // If it doesn't exist, create a new list and add it to the map
                            List<String> newSizeList = new ArrayList<>();
                            newSizeList.add(size);
                            product.getStockStatus().put(color, newSizeList);
                        }

                    }
                }

                for (Map.Entry<String, List<String>> entry : product.getStockStatus().entrySet()) {
                    String color = entry.getKey();
                    List<String> sizeList = entry.getValue();
                    String sizeSummary = String.join(", ", sizeList);
                    stockStatusString.append(color).append(": ").append(sizeSummary).append("\n");
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
                "折合台幣: " + product.getProductPriceInTwd() + "元" + "\n" +
                stockStatusString;
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
        return new JSONObject(jsonString);
    }


}

package com.app.boozespy;

import android.os.AsyncTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DownloadProducts extends AsyncTask<String, Void, List<Product>> {

    protected List<Product> doInBackground(String... searchTerm) {
        List<Product> liquorLandResults = null;
        try {
            liquorLandResults = LiqourLandProducts(searchTerm[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return liquorLandResults;
    }
    
    protected void onPostExecute(List<Product> result) {
        for(Product p : result) {
            System.out.println(p.toString() + '\n');
        }
    }

    //Ubaada's web scrapping work integrated into the app by Sean
    public static List<Product> LiqourLandProducts(String searchTerm) throws IOException {
        List<Product> productList = new ArrayList<>();
        String url = "https://www.shop.liquorland.co.nz/Search.aspx?k=" + searchTerm;
        Map<String, String> cookies = new HashMap<>();
        cookies.put("VisitorIsAdult", "True");
        cookies.put("sessiondob", "01/01/1900");
        cookies.put("selectedStore", "933");
        Document doc = Jsoup.connect(url).cookies(cookies).get();

        Elements scrappedList = doc.getElementsByClass("itemContainer");
        for (Element product : scrappedList) {
            Product newProd = new Product();
            newProd.setName(product.selectFirst(".w2mItemName").text());
            newProd.setPrice(Double.parseDouble(product.selectFirst("span.value , span.SpecialPriceFormat2").text().replace("$","")));
            newProd.setImgUrl("http://www.shop.liquorland.co.nz/" + product.selectFirst("a img").attr("src"));
            newProd.setUrl("http://www.shop.liquorland.co.nz/" + product.selectFirst("a").attr("href"));

            productList.add(newProd);
        }
        return productList;
    }
}
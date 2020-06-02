package com.app.boozespy;

import java.util.regex.*;
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
            liquorLandResults = PaknSaveProducts(searchTerm[0]);
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
            newProd.setStore("Liqour Land");

            productList.add(newProd);
        }
        return productList;
    }

    /*
    Scrapes products from NewWorld's online store with a given search key
     */
    public static List<Product> NewWorldProducts(String searchTerm) throws IOException {
        List<Product> productList = new ArrayList<>();

        String url = "https://www.ishopnewworld.co.nz/Search?s=popularity&sd=0&ps=300&q=" + searchTerm;
        Document doc = Jsoup.connect(url).get();
        Elements scrappedList = doc.select(".fs-product-card");
        System.out.println(scrappedList.size());
        for (Element product : scrappedList) {
            Product newProd = new Product();
            newProd.setName(product.selectFirst(".fs-product-card__description").text());

            String jsonTxt = product.selectFirst(".fs-product-card__footer-container").attr("data-options");
            Matcher priceMatch = Pattern.compile("\"PricePerItem\" : \"([0-9]+.[0-9]+)\"").matcher(jsonTxt);
            while(priceMatch.find()) {
                newProd.setPrice(Double.parseDouble(priceMatch.group(1)));
            }

            String styletxt = product.selectFirst(".fs-product-card__product-image").attr("style");
            Matcher m = Pattern.compile("\\('(http.*)'\\)").matcher(styletxt);
            while(m.find()) {
                newProd.setImgUrl(m.group(1));
            }
            newProd.setUrl("https://www.ishopnewworld.co.nz" + product.selectFirst("a.fs-product-card__details").attr("href"));
            newProd.setStore("New World");
            productList.add(newProd);
        }
        return productList;
    }

    /*
    Scrapes products from PaknSave's online store with a given search key.
    The website is almost identical to NewWorld's.
     */
    public static List<Product> PaknSaveProducts(String searchTerm) throws IOException {
        List<Product> productList = new ArrayList<>();

        String url = "https://www.paknsaveonline.co.nz/Search?q=" + searchTerm;
        Document doc = Jsoup.connect(url).get();
        Elements scrappedList = doc.select(".fs-product-card");
        System.out.println(scrappedList.size());
        for (Element product : scrappedList) {
            Product newProd = new Product();
            newProd.setName(product.selectFirst(".fs-product-card__description").text());

            String jsonTxt = product.selectFirst(".fs-product-card__footer-container").attr("data-options");
            Matcher priceMatch = Pattern.compile("\"PricePerItem\" : \"([0-9]+.[0-9]+)\"").matcher(jsonTxt);
            while (priceMatch.find()) {
                newProd.setPrice(Double.parseDouble(priceMatch.group(1)));
            }

            String styletxt = product.selectFirst(".fs-product-card__product-image").attr("style");
            Matcher m = Pattern.compile("\\('(http.*)'\\)").matcher(styletxt);
            while (m.find()) {
                newProd.setImgUrl(m.group(1));
            }
            newProd.setUrl("https://www.paknsaveonline.co.nz" + product.selectFirst("a.fs-product-card__details").attr("href"));
            newProd.setStore("New World");
            productList.add(newProd);
        }
        return productList;
    }

}

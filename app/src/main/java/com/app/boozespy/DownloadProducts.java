package com.app.boozespy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Notes -----------------------
// For CountDown's website:
// The product are loaded from angular (js) so the scrapper cant see them.
// Can't seem to find the api where the JS reads from.
// Solution: Might have to use a "headless browser" to grab the content after the browser has rendered
// the js and html.

// For henry's:
// Products also loaded dynamically using JS.
// But I was able to find the api address:
//     https://www.henrys.co.nz/api/products?keyword=[keyword]
// But it is only accessible from the JS for some unknown reason.
// Tried to duplicate the cookies but didn't work.
// Solution: "Headless browser"

// For superliqour:
// Products are statically sent. Great!
// But each drink type (ex beer vs wine) is divided into separate pages.
// Solution: Scrapped each type.

/**
 * Async worker for downloading search results for stores websites
 *
 * @author Sean Moir, Ubaada
 */
public class DownloadProducts extends AsyncTask<String, Void, List<Product>> {

    /**
     * This variable is used to access MainActivity methods, Such as updateUI()
     */
    private WeakReference<Context> sender;

    /**
     * Constructor that takes the caller and sets reference to them
     *
     * @param senderContext caller
     */
    public DownloadProducts(Context senderContext) {
        this.sender = new WeakReference<>(senderContext);
    }

    /**
     * main entry point for async worker, calls other functions
     *
     * @param searchTerm item to search for
     * @return list of items found
     */
    protected List<Product> doInBackground(String... searchTerm) {
        List<Product> allResults = new ArrayList<>();
        try {
            // Combine results from all stores
            allResults.addAll(LiqourLandProducts(searchTerm[0]));
            allResults.addAll(NewWorldProducts(searchTerm[0]));
            allResults.addAll(PaknSaveProducts(searchTerm[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allResults;
    }

    /**
     * After async task has finished, send results to UI class and update list of products on UI
     *
     * @param result list of products returned from search
     */
    protected void onPostExecute(List<Product> result) {
        // send result to UI (MainActivity)
        MainActivity parentActivity = (MainActivity) sender.get();
        parentActivity.updateCards(result);
    }

    /**
     * Download image from the url obtained from the webpage
     *
     * @param imgUrl url for image
     * @return image in bitmap format
     */
    public static Bitmap getImageFromUrl(String imgUrl) {
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(imgUrl).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
            // May be replace with a default image here
        }
        return mIcon11;
    }

    /**
     * Scrapes products from Liquorlands's online store with a given search key
     *
     * @param searchTerm item to search for
     * @return list of items found
     * @throws IOException failed to get resources from Liqourland or send request
     */
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
            newProd.setPrice(Double.parseDouble(product.selectFirst("span.value , span.SpecialPriceFormat2").text().replace("$", "")));
            newProd.setImgUrl("https://www.shop.liquorland.co.nz/" + product.selectFirst("a img").attr("src"));
            newProd.setUrl("https://www.shop.liquorland.co.nz/" + product.selectFirst("a").attr("href"));
            newProd.setStore("Liqour Land");
            newProd.setImage(getImageFromUrl(newProd.getImgUrl()));
            productList.add(newProd);
        }
        return productList;
    }

    /**
     * Scrapes products from New World's online store with a given search key
     *
     * @param searchTerm item to search for
     * @return list of items found
     * @throws IOException failed to get resources from New World or send request
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
            // price is in the Json attached to the html code.
            String jsonTxt = product.selectFirst(".fs-product-card__footer-container").attr("data-options");
            Matcher priceMatch = Pattern.compile("\"PricePerItem\" : \"([0-9]+.[0-9]+)\"").matcher(jsonTxt);
            while(priceMatch.find()) {
                newProd.setPrice(Double.parseDouble(priceMatch.group(1)));
            }
            // img url is in the [background-url : ('here')] style attribute of a div
            String styletxt = product.selectFirst(".fs-product-card__product-image").attr("style");
            Matcher m = Pattern.compile("\\('(http.*)'\\)").matcher(styletxt);
            while (m.find()) {
                newProd.setImgUrl(m.group(1));
            }
            newProd.setUrl("https://www.ishopnewworld.co.nz" + product.selectFirst("a.fs-product-card__details").attr("href"));
            newProd.setStore("New World");
            newProd.setImage(getImageFromUrl(newProd.getImgUrl()));
            productList.add(newProd);
        }
        return productList;
    }

    /**
     * Scrapes products from PaknSave's online store with a given search key.
     *
     * @param searchTerm item to search for
     * @return list of items found
     * @throws IOException failed to get resources from Pak 'n' Save or send request
     */
    public static List<Product> PaknSaveProducts(String searchTerm) throws IOException {
        //The website is almost identical to NewWorld's

        List<Product> productList = new ArrayList<>();

        String url = "https://www.paknsaveonline.co.nz/Search?q=" + searchTerm;
        Document doc = Jsoup.connect(url).get();
        Elements scrappedList = doc.select(".fs-product-card");
        System.out.println(scrappedList.size());
        for (Element product : scrappedList) {
            Product newProd = new Product();
            newProd.setName(product.selectFirst(".fs-product-card__description").text());
            // price is in the Json attached to the html code.
            String jsonTxt = product.selectFirst(".fs-product-card__footer-container").attr("data-options");
            Matcher priceMatch = Pattern.compile("\"PricePerItem\" : \"([0-9]+.[0-9]+)\"").matcher(jsonTxt);
            while (priceMatch.find()) {
                newProd.setPrice(Double.parseDouble(priceMatch.group(1)));
            }
            // img url is in the [background-url : ('here')] style attribute of a div
            String styletxt = product.selectFirst(".fs-product-card__product-image").attr("style");
            Matcher m = Pattern.compile("\\('(http.*)'\\)").matcher(styletxt);
            while (m.find()) {
                newProd.setImgUrl(m.group(1));
            }
            newProd.setUrl("https://www.paknsaveonline.co.nz" + product.selectFirst("a.fs-product-card__details").attr("href"));
            newProd.setStore("PaknSave");
            newProd.setImage(getImageFromUrl(newProd.getImgUrl()));
            productList.add(newProd);
        }
        return productList;
    }
}

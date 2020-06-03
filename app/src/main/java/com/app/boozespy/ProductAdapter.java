package com.app.boozespy;


import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * This class is used by ProductViewer (RecycleView) as a data source to display ProductCard(s).
 * <p>
 * Adapters are used in Android world to bridge UI and data.
 * The update methods in this class are called multiple times when UI elements go in and out of user's view,
 * instead of deleting and adding new UI element.
 *
 * @author Ubaada
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    private List<Product> products;

    /**
     * Part of the interface to implement for the ProductViewer to use.
     * Provide a reference to the ProductCard for each product.
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ProductCard productCard;

        public MyViewHolder(ProductCard prodCard) {
            super(prodCard);
            this.productCard = prodCard;
        }
    }

    /**
     * Set the data source (product list).
     * Used after products are returned from DownloadProducts.
     *
     * @param products list of products downloaded
     */
    public ProductAdapter(List<Product> products) {
        this.products = products;
    }

    /**
     * Create new empty ProductCards (invoked by the layout manager)
     *
     * @param parent   reference to parent ViewGroup
     * @param viewType type of view
     * @return Empty product card
     */
    @NonNull
    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        ProductCard prodCard = new ProductCard(parent.getContext());
        return new MyViewHolder(prodCard);
    }

    /**
     * Fill the ProductCard with data.
     * This technique is used to reuse the same UI components and avoid performance lag.
     *
     * @param holder   ProductCard
     * @param position position in list
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.productCard.nameTxt.setText(products.get(position).getName());
        holder.productCard.priceTxt.setText(products.get(position).getPrice() + "$");
        holder.productCard.storeTxt.setText(products.get(position).getStore());
        holder.productCard.imgView.setImageBitmap(products.get(position).getImage());
    }

    /**
     * @return size of your dataset (invoked by the layout manager)
     */
    @Override
    public int getItemCount() {
        return products.size();
    }

}
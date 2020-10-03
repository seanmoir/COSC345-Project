package com.app.boozespy;

import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

/**
 * Desribes the layout for each empty product card in the UI.
 * Card contains a group divided horizontally into 2 groups:
 * 1. For image view.
 * 2. Labels Group
 * Labels Group is then Divided vertically for each label
 * 1. Name
 * 2. Price
 * 3. Store
 *
 * @author Ubaada
 */
public class ProductCard extends CardView {
    public TextView nameTxt;
    public TextView priceTxt;
    public TextView storeTxt;
    public ImageView imgView;
    public TextView branchTxt;
    public TextView distanceTxt;
    /**
     * Configure the card itself
     *
     * @param context parent object
     */
    public ProductCard(Context context) {
        super(context);

        // Configure the card itself
        // Width = same as the product viewer
        // Height = as much space as content takes
        // Set a margin between each card.
        this.setMinimumHeight(200);
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        LinearLayout.LayoutParams cardLayout = new LinearLayout.LayoutParams(width, height);
        cardLayout.setMargins(dpToPx(10,context),dpToPx(10,context),dpToPx(10,context),dpToPx(10,context));
        this.setLayoutParams(cardLayout);


        // Group of texts (name, price, store) divided vertically.
        LinearLayout vertDivide = new LinearLayout(context);
        vertDivide.setOrientation(LinearLayout.VERTICAL);
        vertDivide.setDividerPadding(dpToPx(10,context));
        vertDivide.setMinimumHeight(-2);
        vertDivide.addView(this.nameTxt = new TextView(context));
        nameTxt.setTypeface(null, Typeface.BOLD);
        vertDivide.addView(this.priceTxt = new TextView(context));
        vertDivide.addView(this.storeTxt = new TextView(context));
        vertDivide.addView(this.branchTxt = new TextView(context));
        vertDivide.addView(this.distanceTxt = new TextView(context));

        // Sets fixed image size dp=200 x dp=200.
        // Sets a margin (white space) around image
        imgView = new ImageView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dpToPx(200,context), dpToPx(200,context));
        layoutParams.setMargins(dpToPx(100,context),dpToPx(100,context),dpToPx(100,context),dpToPx(100,context));
        imgView.setLayoutParams(layoutParams);

        // Contains Image on the left and text on the right
        LinearLayout horDivide = new LinearLayout(context);
        horDivide.setOrientation(LinearLayout.HORIZONTAL);
        horDivide.addView(imgView);
        horDivide.addView(vertDivide);

        this.addView(horDivide);

    }

    /**
     * converter class
     *
     * @param dp      dp value
     * @param context parent object
     * @return px value
     */
    public static int dpToPx(float dp, Context context) {
        float answer = dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) dp;
    }
}
package com.example.myapplication.Compare;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {
    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        super(context, 0, productList);
        this.context = context;
        this.productList = productList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Product product = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_product, parent, false);
        }

        ImageView image = convertView.findViewById(R.id.product_image);
        TextView title = convertView.findViewById(R.id.product_title);
        TextView price = convertView.findViewById(R.id.product_price);
        TextView mallName = convertView.findViewById(R.id.product_mall_name);

        title.setText(product.getTitle());
        price.setText("가격: " + product.getPrice());
        mallName.setText(product.getMallName());

        // 이미지 로드
        Glide.with(context)
                .load(product.getImageUrl())
                .into(image);

        convertView.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(product.getLink()));
            context.startActivity(browserIntent);
        });

        return convertView;
    }
}

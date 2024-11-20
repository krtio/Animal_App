package com.example.myapplication.Home.Animal;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import java.util.List;

public class BannerPagerAdapter extends PagerAdapter {

    private Context context;
    private List<AnimalItem> animalItems;

    public BannerPagerAdapter(Context context, List<AnimalItem> animalItems) {
        this.context = context;
        this.animalItems = animalItems;
    }

    @Override
    public int getCount() {
        return animalItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.banner_itmes, container, false);

        ImageView imageView = view.findViewById(R.id.bannerImageView);
        TextView stateTextView = view.findViewById(R.id.stateTextView);
        TextView shelterNameTextView = view.findViewById(R.id.shelterNameTextView);
        TextView ageTextView = view.findViewById(R.id.ageTextView);
        TextView speciesTextView = view.findViewById(R.id.speciesTextView);
        TextView endDateTextView = view.findViewById(R.id.endDateTextView); // ID 변경
        TextView sexTextView = view.findViewById(R.id.sexTextView);

        AnimalItem animalItem = animalItems.get(position);

        Glide.with(context)
                .load(animalItem.getImageCours())
                .into(imageView);

        sexTextView.setText("성별: " + animalItem.getSexNm());
        ageTextView.setText("나이: " + animalItem.getAgeInfo());
        speciesTextView.setText("품종 : "+animalItem.getSpeciesNm());
        stateTextView.setText(animalItem.getStateNm());
        shelterNameTextView.setText(animalItem.getShterNm());
        endDateTextView.setText("보호 기간: " + animalItem.getPblancEndDe());

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}


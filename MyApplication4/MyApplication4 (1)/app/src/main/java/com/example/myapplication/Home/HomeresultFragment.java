package com.example.myapplication.Home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.util.ArrayList;

public class HomeresultFragment extends AppCompatActivity {

    private TextView animalResultTextView;
    private ImageView animalImageView;
    private Button mainScreenButton;
    private Button retryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_homeresult);

        animalResultTextView = findViewById(R.id.animalresult);
        animalImageView = findViewById(R.id.animalImage);
        mainScreenButton = findViewById(R.id.mainScreenButton);
        retryButton = findViewById(R.id.retryButton);

        Intent intent = getIntent();
        ArrayList<Integer> answerScores = intent.getIntegerArrayListExtra("answerScores");

        if (answerScores != null) {
            displayResultBasedOnScores(answerScores);
        }

        mainScreenButton.setOnClickListener(v -> {
            finish();
        });

        retryButton.setOnClickListener(v -> {
            Intent retryIntent = new Intent(HomeresultFragment.this, HomeFragment1.class);
            startActivity(retryIntent);
            finish();
        });
    }

    private void displayResultBasedOnScores(ArrayList<Integer> scores) {
        String resultText;
        int resultImage;

        if ((scores.get(0) == 1 || scores.get(0) == 2) &&
                (scores.get(1) == 2 || scores.get(1) == 3) &&
                (scores.get(2) == 1 || scores.get(2) == 2) &&
                (scores.get(3) == 1 || scores.get(3) == 2) &&
                scores.get(4) == 2) {
            resultText = "고양이와 잘 어울리는 성향입니다!";
            resultImage = R.drawable.cat_image;

        } else if ((scores.get(0) == 1 || scores.get(0) == 2) &&
                (scores.get(1) == 1 || scores.get(1) == 2) &&
                (scores.get(2) == 2 || scores.get(2) == 3) &&
                (scores.get(3) == 1 || scores.get(3) == 2) &&
                scores.get(4) == 1) {

            resultText = "강아지와 잘 어울리는 성향입니다!";
            resultImage = R.drawable.dog_image;

        } else if ((scores.get(0) == 2 || scores.get(0) == 3) &&
                (scores.get(1) == 1 || scores.get(1) == 2 || scores.get(1) == 3) &&
                (scores.get(2) == 2 || scores.get(2) == 3) &&
                scores.get(3) == 3 &&
                scores.get(4) == 3) {

            resultText = "물고기를 키워보시는 건 어떨까요?";
            resultImage = R.drawable.fish_image;

        } else if (scores.get(0) == 3 &&
                scores.get(1) == 3 &&
                (scores.get(2) == 1 || scores.get(2) == 2) &&
                (scores.get(3) == 1 || scores.get(3) == 2) &&
                scores.get(4) == 3) {

            resultText = "토끼가 어울리는 성향입니다!";
            resultImage = R.drawable.rabbit_image;

        } else {
            resultText = "적합한 동물을 찾지 못했어요.";
            resultImage = R.drawable.duck_image;
        }

        animalResultTextView.setText(resultText);
        animalImageView.setImageResource(resultImage);
    }
}

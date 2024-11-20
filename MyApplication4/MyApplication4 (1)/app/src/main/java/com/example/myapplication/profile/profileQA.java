package com.example.myapplication.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class profileQA extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile_qa);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        LinearLayout question1 = findViewById(R.id.faq1);
        final TextView answer1 = findViewById(R.id.answer1);
        final ImageView arrow1 = findViewById(R.id.arrow1);
        question1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleAnswer(answer1, arrow1);
            }
        });

        LinearLayout question2 = findViewById(R.id.faq2);
        final TextView answer2 = findViewById(R.id.answer2);
        final ImageView arrow2 = findViewById(R.id.arrow2);
        question2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleAnswer(answer2, arrow2);
            }
        });

        LinearLayout question3 = findViewById(R.id.faq3);
        final TextView answer3 = findViewById(R.id.answer3);
        final ImageView arrow3 = findViewById(R.id.arrow3);
        question3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleAnswer(answer3, arrow3);
            }
        });

        LinearLayout question4 = findViewById(R.id.faq4);
        final TextView answer4 = findViewById(R.id.answer4);
        final ImageView arrow4 = findViewById(R.id.arrow4);
        question4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleAnswer(answer4, arrow4);
            }
        });

    }

    private void toggleAnswer(TextView answer, ImageView arrow) {
        if (answer.getVisibility() == View.GONE) {
            answer.setVisibility(View.VISIBLE);
            arrow.setImageResource(R.drawable.uparrow);
        } else {
            answer.setVisibility(View.GONE);
            arrow.setImageResource(R.drawable.downarrow);
        }
    }
}
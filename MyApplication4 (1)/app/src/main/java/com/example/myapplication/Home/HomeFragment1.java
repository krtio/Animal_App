package com.example.myapplication.Home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.util.ArrayList;

public class HomeFragment1 extends AppCompatActivity {

    private RadioGroup radioGroup;
    private Button nextButton;
    private TextView questionNumberTextView, questionTextView;
    private ArrayList<Integer> answerScores;  // 숫자 배열로 저장
    private int questionCount = 1;

    // 질문 목록
    private final String[] questions = {
            "당신은 청결하게 방을 사용하시나요?",
            "외출을 좋아하시나요?",
            "평소 집에 얼마나 계신가요?",
            "반려동물을 키우는 목적이 무엇인가요?",
            "당신의 반려동물에게 가장 원하는 것은?"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home1);

        // UI 요소 초기화
        radioGroup = findViewById(R.id.initial_radio_group);
        nextButton = findViewById(R.id.next_button);
        questionNumberTextView = findViewById(R.id.question_number);
        questionTextView = findViewById(R.id.question);


        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        // 숫자 배열 초기화
        answerScores = new ArrayList<>();

        // 첫 번째 질문 표시
        updateQuestion();

        // "다음" 버튼 클릭 시 동작
        nextButton.setOnClickListener(v -> {
            // 선택된 라디오 버튼의 ID 가져오기
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton selectedRadioButton = findViewById(selectedId);
                int selectedScore = radioGroup.indexOfChild(selectedRadioButton) + 1; // 1, 2, 3 값 저장

                // 선택된 값을 배열에 추가
                answerScores.add(selectedScore);

                // 마지막 질문일 경우 결과 화면으로 이동
                if (questionCount == questions.length) {
                    Intent resultIntent = new Intent(HomeFragment1.this, HomeresultFragment.class);
                    resultIntent.putIntegerArrayListExtra("answerScores", answerScores);
                    startActivity(resultIntent);
                    finish();
                } else {
                    // 다음 질문으로 이동
                    questionCount++;
                    updateQuestion();
                }
            }
        });
    }

    private void updateQuestion() {
        // 질문 번호 및 내용 설정
        questionNumberTextView.setText(questionCount + "/" + questions.length);
        questionTextView.setText(questions[questionCount - 1]);

        // 이전에 선택된 라디오 버튼 해제
        radioGroup.clearCheck();

        // 각 질문에 맞는 라디오 버튼 텍스트 설정
        switch (questionCount) {
            case 1:
                ((RadioButton) findViewById(R.id.radio_option_1)).setText("깨끗하게 사용하는 편입니다.");
                ((RadioButton) findViewById(R.id.radio_option_2)).setText("중간입니다.");
                ((RadioButton) findViewById(R.id.radio_option_3)).setText("더러운 편입니다.");
                break;
            case 2:
                ((RadioButton) findViewById(R.id.radio_option_1)).setText("좋아합니다.");
                ((RadioButton) findViewById(R.id.radio_option_2)).setText("보통입니다.");
                ((RadioButton) findViewById(R.id.radio_option_3)).setText("좋아하지 않습니다.");
                break;
            case 3:
                ((RadioButton) findViewById(R.id.radio_option_1)).setText("거의 항상 집에 있습니다.");
                ((RadioButton) findViewById(R.id.radio_option_2)).setText("중간 정도입니다.");
                ((RadioButton) findViewById(R.id.radio_option_3)).setText("집을 비우는 경우가 잦습니다.");
                break;
            case 4:
                ((RadioButton) findViewById(R.id.radio_option_1)).setText("외로움 해소");
                ((RadioButton) findViewById(R.id.radio_option_2)).setText("동물을 좋아해서");
                ((RadioButton) findViewById(R.id.radio_option_3)).setText("그 외");
                break;
            case 5:
                ((RadioButton) findViewById(R.id.radio_option_1)).setText("말을 잘 들었으면 좋겠어요.");
                ((RadioButton) findViewById(R.id.radio_option_2)).setText("애교가 많았으면 좋겠어요.");
                ((RadioButton) findViewById(R.id.radio_option_3)).setText("방을 어지럽히지 않았으면 좋겠어요.");
                break;
        }
    }
}

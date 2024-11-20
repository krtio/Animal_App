package com.example.myapplication.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.profile.profileQA;

public class profile extends Fragment {

    private TextView tvNickName;
    private TextView qaButton;  // TextView로 변경

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank5, container, false);

        tvNickName = view.findViewById(R.id.nickname);
        qaButton = view.findViewById(R.id.faq);  // TextView로 변경

        // qaButton이 클릭되면 profileQA 화면으로 이동
        qaButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), profileQA.class);  // profileQA로 이동
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadUserProfile();
    }

    // 사용자 프로필을 SharedPreferences에서 로드
    private void loadUserProfile() {
        SharedPreferences prefs = getActivity().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE);
        String userName = prefs.getString("logged_in_user_name", "사용자 이름 없음");
        tvNickName.setText(userName);  // 프로필 이름 설정
    }

    // 사용자 이름 업데이트
    public void updateUserName(String userName) {
        tvNickName.setText(userName);
    }
}

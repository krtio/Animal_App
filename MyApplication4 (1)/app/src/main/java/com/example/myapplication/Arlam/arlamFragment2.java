package com.example.myapplication.Arlam;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class arlamFragment2 extends Fragment {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> chatRoomList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_arlam2, container, false);

        listView = view.findViewById(R.id.listView_chat_rooms2);

        chatRoomList = new ArrayList<>();
        chatRoomList.add("🎉 이벤트 알림: 여름 세일 최대 50% 할인! (오늘 10:00)");
        chatRoomList.add("🔥 할인 알림: 오늘 하루만 30% 할인! (어제 18:30)");
        chatRoomList.add("💥 특별 이벤트: 무료 배송 쿠폰 제공! (어제 14:45)");

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, chatRoomList);
        listView.setAdapter(adapter);

        return view;
    }
}

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

public class alram_fragment extends Fragment {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> chatRoomList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_arlam_fragment, container, false);

        listView = view.findViewById(R.id.listView_chat_rooms);

        chatRoomList = new ArrayList<>();
        chatRoomList.add("채팅방 1 : 산책 모임 (오전 10:30)");

        // 어댑터 설정
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, chatRoomList);
        listView.setAdapter(adapter);

        return view;
    }
}


// 개발 진행중 ( 미완성 )
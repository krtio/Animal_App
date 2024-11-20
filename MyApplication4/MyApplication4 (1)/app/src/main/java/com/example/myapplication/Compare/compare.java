package com.example.myapplication.Compare;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import android.widget.Spinner;
import android.util.Log;

import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class compare extends Fragment {
    private Spinner spinner;
    private ListView listView;
    private ProductAdapter adapter;
    private List<Product> productList;
    private boolean isLoading = false;

    private static final String CLIENT_ID = "XUALd2qXQlC_sDuy4uKw";
    private static final String CLIENT_SECRET = "WCLP2OcIaR";

    private static final String DOG_FOOD_URL = "https://openapi.naver.com/v1/search/shop.json?query=강아지%20사료&sort=sim&display=15";
    private static final String DOG_SUPPLEMENT_URL = "https://openapi.naver.com/v1/search/shop.json?query=강아지%20영양제&sort=sim&display=15";
    private static final String DOG_GROOMING_URL = "https://openapi.naver.com/v1/search/shop.json?query=강아지%20미용용품&sort=sim&display=15";
    private static final String DOG_OTHER_URL = "https://openapi.naver.com/v1/search/shop.json?query=강아지%20집&sort=sim&display=15";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank2, container, false);

        spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.animal_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        listView = view.findViewById(R.id.list_view);
        productList = new ArrayList<>();
        this.adapter = new ProductAdapter(requireActivity(), productList);
        listView.setAdapter(this.adapter);

        SearchView searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    String selectedCategory = spinner.getSelectedItem().toString();
                    String modifiedQuery = selectedCategory + " " + query;
                    startSearchResultActivity(modifiedQuery);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        view.findViewById(R.id.photoButton1).setOnClickListener(v -> {
            productList.clear();
            fetchDogFood(DOG_FOOD_URL);
        });

        view.findViewById(R.id.photoButton2).setOnClickListener(v -> {
            productList.clear();
            fetchDogFood(DOG_SUPPLEMENT_URL);
        });

        view.findViewById(R.id.photoButton3).setOnClickListener(v -> {
            productList.clear();
            fetchDogFood(DOG_GROOMING_URL);
        });

        view.findViewById(R.id.photoButton4).setOnClickListener(v -> {
            productList.clear();
            fetchDogFood(DOG_OTHER_URL);
        });

        setupListViewScrollListener();

        return view;
    }

    private void startSearchResultActivity(String query) {
        Intent intent = new Intent(getActivity(), SearchResultFragment.class);
        intent.putExtra("SEARCH_QUERY", query);
        startActivity(intent);
    }

    private void setupListViewScrollListener() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount && !isLoading) {
                    isLoading = true;
                    fetchDogFood(DOG_FOOD_URL); // 기본 URL, 필요 시 변경
                }
            }
        });
    }

    private void fetchDogFood(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-Naver-Client-Id", CLIENT_ID)
                .addHeader("X-Naver-Client-Secret", CLIENT_SECRET)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("네트워크 오류", "강아지 사료 가져오기 실패", e);
                isLoading = false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.d("API 응답", responseBody); // 응답 내용 로그에 출력

                    // 응답이 JSON 형식인지 확인
                    if (responseBody.startsWith("{") || responseBody.startsWith("[")) {
                        try {
                            JSONObject jsonObject = new JSONObject(responseBody);
                            JSONArray items = jsonObject.getJSONArray("items");

                            // 중복 데이터 추가 방지
                            HashSet<String> existingTitles = new HashSet<>();
                            for (Product product : productList) {
                                existingTitles.add(product.getTitle());
                            }

                            for (int i = 0; i < items.length(); i++) {
                                JSONObject item = items.getJSONObject(i);
                                String title = item.getString("title").replaceAll("<.*?>", "");
                                String imageUrl = item.getString("image");
                                String price = item.getString("lprice");
                                String mallName = item.optString("mallName", "알 수 없음");
                                String link = item.getString("link");

                                // 중복 데이터 추가 방지
                                if (!existingTitles.contains(title)) {
                                    productList.add(new Product(title, imageUrl, price, mallName, link));
                                    existingTitles.add(title); // 추가 후 기존 목록에 등록
                                }
                            }

                            requireActivity().runOnUiThread(() -> {
                                adapter.notifyDataSetChanged();
                                isLoading = false;
                            });
                        } catch (JSONException e) {
                            Log.e("JSON 오류", "JSON 파싱 실패", e);
                            isLoading = false;
                        }
                    } else {
                        Log.e("API 오류", "JSON이 아닌 응답 수신: " + responseBody);
                        isLoading = false;
                    }
                } else {
                    Log.e("API 오류", "응답 코드: " + response.code());
                    isLoading = false;
                }
            }
        });
    }
}

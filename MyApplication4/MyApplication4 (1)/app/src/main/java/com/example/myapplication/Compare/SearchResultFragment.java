package com.example.myapplication.Compare;

import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchResultFragment extends AppCompatActivity {

    private static final String CLIENT_ID = "XUALd2qXQlC_sDuy4uKw";
    private static final String CLIENT_SECRET = "WCLP2OcIaR";
    private ListView listView;
    private ProductAdapter adapter;

    private List<Product> productList;
    private int currentPage = 1;
    private boolean isLoading = false; // 데이터 로딩 중인지 확인
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_compare_result);

        listView = findViewById(R.id.list_view);
        productList = new ArrayList<>();

        query = getIntent().getStringExtra("SEARCH_QUERY");
        fetchSearchResults(currentPage);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount && !isLoading) {
                    isLoading = true;
                    currentPage++;
                    fetchSearchResults(currentPage);
                }
            }
        });
    }

    private void fetchSearchResults(int page) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://openapi.naver.com/v1/search/shop.json?query=" + query + "&display=20&start=" + (page - 1) * 20;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-Naver-Client-Id", CLIENT_ID)
                .addHeader("X-Naver-Client-Secret", CLIENT_SECRET)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                isLoading = false; // 에러 발생 시 로딩 상태 초기화
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseBody);
                        JSONArray items = jsonObject.getJSONArray("items");

                        for (int i = 0; i < items.length(); i++) {
                            JSONObject item = items.getJSONObject(i);
                            String title = item.getString("title").replaceAll("<.*?>", "");
                            String imageUrl = item.getString("image");
                            String price = item.getString("lprice");
                            String mallName = item.optString("mallName", "Unknown");
                            String link = item.getString("link");

                            productList.add(new Product(title, imageUrl, price, mallName, link));
                        }

                        runOnUiThread(() -> {
                            if (adapter == null) {
                                adapter = new ProductAdapter(SearchResultFragment.this, productList);
                                listView.setAdapter(adapter);
                            } else {
                                adapter.notifyDataSetChanged(); // 데이터가 변경되었음을 어댑터에 알림
                            }
                            isLoading = false; // 로딩 상태 초기화
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                        isLoading = false; // 에러 발생 시 로딩 상태 초기화
                    }
                }
            }
        });
    }
}

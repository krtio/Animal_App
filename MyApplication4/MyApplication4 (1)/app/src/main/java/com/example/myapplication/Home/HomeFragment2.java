package com.example.myapplication.Home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.myapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HomeFragment2 extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private ListView hospitalListView;
    private ArrayList<String> hospitalDetails = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home2);

        // 지도 초기화
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // 리스트뷰 설정
        hospitalListView = findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, hospitalDetails);
        hospitalListView.setAdapter(adapter);

        // 동물병원 찾기 버튼 클릭 리스너
        Button animalHospitalButton = findViewById(R.id.animal_hospital_button);
        animalHospitalButton.setOnClickListener(v -> findNearbyAnimalHospitals());

        // 반려동물 친화적 장소 버튼 클릭 리스너
        Button petFriendlyButton = findViewById(R.id.pet_friendly_button);
        petFriendlyButton.setOnClickListener(v -> findPetFriendlyLocations());

        // 리스트뷰 항목 클릭 시 전화걸기
        hospitalListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedHospital = hospitalDetails.get(position);
            String[] parts = selectedHospital.split(" - ");
            if (parts.length > 1) {
                String phoneNumber = parts[1]; // 전화번호가 리스트에 포함되어 있다고 가정

                // 전화걸기 화면 띄우기
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(dialIntent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // 위치 권한이 허용된 경우 내 위치 표시
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        LatLng defaultLocation = new LatLng(-34, 151); // 기본 위치
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10));
        mMap.addMarker(new MarkerOptions()
                .position(defaultLocation)
                .title("기본 위치")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
    }

    private void resetMapAndListView() {
        mMap.clear();
        hospitalDetails.clear();
        adapter.notifyDataSetChanged();
    }

    // 동물병원 찾기
    private void findNearbyAnimalHospitals() {
        resetMapAndListView();
        LatLng currentLatLng = mMap.getCameraPosition().target;
        mMap.addMarker(new MarkerOptions()
                .position(currentLatLng)
                .title("현재 위치")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

        String apiKey = "AIzaSyBZ4m1YILBLH1DGF1H13Tvl6ZYyCjLXetY";
        String placeType = "veterinary_care";
        String location = currentLatLng.latitude + "," + currentLatLng.longitude;
        String radius = "1500";

        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + location + "&radius=" + radius + "&type=" + placeType + "&key=" + apiKey;

        new Thread(() -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");
                connection.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                reader.close();

                JSONObject jsonResponse = new JSONObject(responseBuilder.toString());
                JSONArray results = jsonResponse.getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {
                    JSONObject hospital = results.getJSONObject(i);
                    String name = hospital.getString("name");
                    String phoneNumber = hospital.optString("vicinity", "전화번호 없음");

                    // 위도와 경도 정보
                    JSONObject locationObject = hospital.getJSONObject("geometry").getJSONObject("location");
                    double lat = locationObject.getDouble("lat");
                    double lng = locationObject.getDouble("lng");
                    LatLng hospitalLocation = new LatLng(lat, lng);

                    runOnUiThread(() -> {
                        mMap.addMarker(new MarkerOptions()
                                .position(hospitalLocation)
                                .title(name)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                        // 병원 이름과 전화번호를 리스트에 추가
                        hospitalDetails.add(name + " - " + phoneNumber);
                        adapter.notifyDataSetChanged();
                    });
                }

                runOnUiThread(() -> mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15)));

            } catch (Exception e) {
                Log.e("API Error", e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    // 반려동물 친화적인 장소 찾기
    private void findPetFriendlyLocations() {
        resetMapAndListView();
        LatLng currentLatLng = mMap.getCameraPosition().target;
        mMap.addMarker(new MarkerOptions()
                .position(currentLatLng)
                .title("현재 위치")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

        String apiKey = "2J%2BDVIQdsXNfqLa5U%2F8lvbfy4ZlF0DfGmdTWnKAos4BhmUqOUhChmrBZv%2FahjzbVX9IRbJRNio2OguiiZ3XhjQ%3D%3D";
        int totalPages = 3;

        for (int pageNo = 1; pageNo <= totalPages; pageNo++) {
            int finalPageNo = pageNo;
            new Thread(() -> {
                try {
                    String url = "http://apis.data.go.kr/B551011/KorPetTourService/locationBasedList" +
                            "?numOfRows=10&pageNo=" + finalPageNo + "&MobileOS=AND&MobileApp=AppTest&serviceKey=" + apiKey +
                            "&_type=json&listYN=Y&arrange=C&mapX=" + currentLatLng.longitude +
                            "&mapY=" + currentLatLng.latitude + "&radius=15000";

                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.connect();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder responseBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }
                    reader.close();

                    JSONObject jsonResponse = new JSONObject(responseBuilder.toString());
                    JSONObject responseBody = jsonResponse.getJSONObject("response");
                    JSONObject body = responseBody.getJSONObject("body");
                    JSONArray items = body.getJSONObject("items").getJSONArray("item");

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        String name = item.getString("title");
                        String addr1 = item.optString("addr1", "주소 없음");
                        String addr2 = item.optString("addr2", "");
                        String fullAddress = addr1 + (addr2.isEmpty() ? "" : ", " + addr2);
                        double lat = item.getDouble("mapy");
                        double lng = item.getDouble("mapx");
                        LatLng location = new LatLng(lat, lng);

                        runOnUiThread(() -> {
                            mMap.addMarker(new MarkerOptions()
                                    .position(location)
                                    .title(name)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                            hospitalDetails.add(name + " - " + fullAddress);
                            adapter.notifyDataSetChanged();
                        });
                    }
                } catch (Exception e) {
                    Log.e("API Error", e.getMessage());
                    e.printStackTrace();
                }
            }).start();
        }
    }
}

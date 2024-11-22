package com.example.myapplication.Home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.viewpager.widget.ViewPager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import android.util.Log;
import com.example.myapplication.Home.Animal.AnimalResponse;
import com.example.myapplication.Home.Animal.BannerPagerAdapter;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.login.login_activity;

public class home extends Fragment {

    private static final String API_URL = "https://openapi.gg.go.kr/AbdmAnimalProtect";
    private static final String KEY = "d6b1dc6fce79450ea1bc94527fb61fc8"; // 인증키
    private TextView loginTextView;
    private TextView logoutTextView;
    private View loginPrompt;
    private View petList;
    private View mypetlist;
    private ViewPager viewPager;
    private BannerPagerAdapter bannerAdapter;

    public home() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank1, container, false);

        loginTextView = view.findViewById(R.id.sign_in_create_account);
        logoutTextView = view.findViewById(R.id.logoutTextView);
        loginPrompt = view.findViewById(R.id.loginPrompt);
        mypetlist = view.findViewById(R.id.mypetlist);
        petList = view.findViewById(R.id.petList);
        viewPager = view.findViewById(R.id.viewPager);

        ImageButton photoButton1 = view.findViewById(R.id.photoButton1);
        ImageButton photoButton2 = view.findViewById(R.id.photoButton2);

        checkLoginStatus();  // 로그인 상태 확인

        loginTextView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), login_activity.class);
            startActivityForResult(intent, 1);
        });

        photoButton1.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HomeFragment1.class);
            startActivity(intent);
        });

        photoButton2.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HomeFragment2.class);
            startActivity(intent);
        });

        logoutTextView.setOnClickListener(v -> logoutUser());

        fetchAnimalData(); // API 데이터 가져오기

        return view;
    }

    private void checkLoginStatus() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", requireActivity().MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false); // 로그인 상태 확인

        if (isLoggedIn) {
            onLoginSuccess();
        } else {
            onLogout();
        }
    }

    private void logoutUser() {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.logout();
            onLogout();
        }
    }

    private void onLoginSuccess() {
        loginTextView.setVisibility(View.GONE);
        logoutTextView.setVisibility(View.VISIBLE);
        loginPrompt.setVisibility(View.GONE);
        mypetlist.setVisibility(View.VISIBLE);
        petList.setVisibility(View.VISIBLE);
    }

    private void onLogout() {
        loginTextView.setVisibility(View.VISIBLE);
        logoutTextView.setVisibility(View.GONE);
        loginPrompt.setVisibility(View.VISIBLE);
        mypetlist.setVisibility(View.GONE);
        petList.setVisibility(View.GONE);
    }

    private void fetchAnimalData() {
        new Thread(() -> {
            try {
                String url = API_URL + "?KEY=" + KEY + "&Type=xml&pIndex=1&pSize=10";
                Request request = new Request.Builder().url(url).build();

                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();

                if (response.isSuccessful() && response.body() != null) {
                    String xmlResponse = response.body().string();
                    Log.d("XML Response", xmlResponse);

                    Serializer serializer = new Persister();
                    AnimalResponse animalResponse = serializer.read(AnimalResponse.class, xmlResponse);

                    requireActivity().runOnUiThread(() -> {
                        if (animalResponse.getAnimals() != null) {
                            bannerAdapter = new BannerPagerAdapter(requireActivity(), animalResponse.getAnimals());
                            viewPager.setAdapter(bannerAdapter);
                        }
                    });
                } else {
                    Log.e("API Call", "Failed to fetch data");
                }
            } catch (Exception e) {
                Log.e("API Call", "Error: " + e.getMessage());
            }
        }).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK) {
            onLoginSuccess();
        }
    }
}

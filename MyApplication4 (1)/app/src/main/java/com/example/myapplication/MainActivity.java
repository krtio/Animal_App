package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.app.AlertDialog;
import com.example.myapplication.Arlam.arlam;
import com.example.myapplication.Community.CreatePostActivity;
import com.example.myapplication.Community.PreviewPostActivity;
import com.example.myapplication.Community.community;
import com.example.myapplication.Compare.compare;
import com.example.myapplication.Home.home;
import com.example.myapplication.profile.profile;
import com.example.myapplication.login.login_activity;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    SparseArray<Fragment> fragmentArray = new SparseArray<>();
    private String loggedInUserName;

    private static final int REQUEST_CODE_PERMISSIONS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentArray.put(R.id.home, new home());
        fragmentArray.put(R.id.compare, new compare());
        fragmentArray.put(R.id.community, new community());
        fragmentArray.put(R.id.arlam, new arlam());
        fragmentArray.put(R.id.profile, new profile());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_layout, fragmentArray.get(R.id.home))
                    .commit();
        }

        NavigationBarView navigationBarView = findViewById(R.id.bottomNavigationView);
        navigationBarView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = fragmentArray.get(item.getItemId());
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_layout, selectedFragment)
                        .commit();
                return true;
            }
            return false;
        });

        loggedInUserName = getLoggedInUserName();

        // 권한 요청
        requestPermissionsIfNeeded();
    }

    public void setLoggedInUserName(String name) {
        this.loggedInUserName = name;
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("logged_in_user_name", name);
        editor.putBoolean("is_logged_in", true);
        editor.apply();
    }

    public String getLoggedInUserName() {
        if (loggedInUserName == null) {
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            loggedInUserName = prefs.getString("logged_in_user_name", null);
        }
        return loggedInUserName;
    }

    public void startPreviewPostActivity() {
        Intent intent = new Intent(this, PreviewPostActivity.class);
        intent.putExtra("user_name", getLoggedInUserName()); // 사용자 이름 전달
        startActivity(intent);
    }

    public void logout() {
        loggedInUserName = null;

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("is_logged_in", false);
        editor.putString("logged_in_user_name", "");
        editor.apply();

        Intent intent = new Intent(this, login_activity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String userName = data.getStringExtra("user_name");
            setLoggedInUserName(userName);
            Fragment profileFragment = fragmentArray.get(R.id.profile);
            if (profileFragment != null) {
                ((profile) profileFragment).updateUserName(userName);
            }
        }
    }

    // 권한 요청 처리
    private void requestPermissionsIfNeeded() {
        // 카메라, 위치, 갤러리 권한 요청
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[] {
                            Manifest.permission.CAMERA,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    REQUEST_CODE_PERMISSIONS
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                new AlertDialog.Builder(this)
                        .setTitle("권한 거부")
                        .setMessage("이 앱은 카메라, 위치 및 갤러리 권한을 요구합니다. 설정에서 권한을 허용해주세요.")
                        .setPositiveButton("설정", (dialog, which) -> {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", getPackageName(), null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        })
                        .setNegativeButton("취소", null)
                        .show();
            }
        }
    }
}

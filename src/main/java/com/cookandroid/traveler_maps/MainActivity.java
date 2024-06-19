package com.cookandroid.traveler_maps;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView progressTextView;
    private Button gangwonButton;
    private static final String PREFS_NAME = "GangwonPrefs";
    private static final String IS_FILLED_KEY = "IsFilledArray";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        progressTextView = findViewById(R.id.progressTextView); // TextView 초기화
        gangwonButton = findViewById(R.id.gangwonButton); // Button 초기화

        gangwonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GangwonActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 강원도 상태를 다시 로드하고 텍스트뷰를 업데이트
        boolean[] gangwonIsFilled = loadGangwonIsFilledArray();
        updateProgressTextView(gangwonIsFilled);
    }

    private boolean[] loadGangwonIsFilledArray() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedString = prefs.getString(IS_FILLED_KEY, null);
        boolean[] gangwonIsFilled = new boolean[18]; // 강원도에 18개 시/군/구

        if (savedString != null) {
            String[] split = savedString.split(",");
            for (int i = 0; i < split.length; i++) {
                gangwonIsFilled[i] = Boolean.parseBoolean(split[i]);
            }
        }

        return gangwonIsFilled;
    }

    private void saveGangwonIsFilledArray(boolean[] gangwonIsFilled) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        StringBuilder sb = new StringBuilder();
        for (boolean b : gangwonIsFilled) {
            sb.append(b).append(",");
        }
        editor.putString(IS_FILLED_KEY, sb.toString());
        editor.apply();
    }

    private void updateProgressTextView(boolean[] gangwonIsFilled) {
        int selectedCount = 0;
        for (boolean filled : gangwonIsFilled) {
            if (filled) {
                selectedCount++;
            }
        }
        int totalRegions = gangwonIsFilled.length;
        int progressPercentage = (selectedCount * 100) / totalRegions;
        progressTextView.setText("진행도: " + progressPercentage + "%");
    }
}

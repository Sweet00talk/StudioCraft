package com.example.studiocraft;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ObsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_obs);

        // Menggunakan ID root layout sedia ada / Elakkan crash jika R.id.main tiada
        if (findViewById(R.id.main) != null) {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
        // =========================================================
        // SAMBUNGKAN NAVIGASI BAR DI BAHAGIAN BAWAH
        // =========================================================
        LinearLayout navHome = findViewById(R.id.nav_home);
        LinearLayout navTask = findViewById(R.id.nav_task);
        LinearLayout navHelp = findViewById(R.id.nav_help);

        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            });
        }

        if (navTask != null) {
            navTask.setOnClickListener(v -> {
                Intent intent = new Intent(ObsActivity.this, ChallengeActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            });
        }

        if (navHelp != null) {
            navHelp.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), HelpActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            });
        }


        // Set Animasi
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.fade_slide_up);

        // ID dikemaskini ke tv_title_settings ikut XML
        TextView tvTitle = findViewById(R.id.tv_title_settings);
        if (tvTitle != null) {
            tvTitle.startAnimation(slideUp);
        }

        // ID dikemaskini ke btn_watch_tutorial ikut XML
        AppCompatButton btnTutorial = findViewById(R.id.btn_tutorial);
        if (btnTutorial != null) {
            btnTutorial.startAnimation(slideUp);
            btnTutorial.setOnClickListener(v -> {
                Intent intent = new Intent(ObsActivity.this, ObsTutorialActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            });
        }
    }
}
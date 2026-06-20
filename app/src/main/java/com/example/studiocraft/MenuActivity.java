package com.example.studiocraft;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // =========================================================
        // PANGGIL ANIMASI NAIK DARI BAWAH
        // =========================================================
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.fade_slide_up);

        // 1. Jalankan animasi pada tajuk "MENU SELECT"
        TextView tvTitle = findViewById(R.id.tv_title);
        if (tvTitle != null) {
            tvTitle.startAnimation(slideUp);
        }

        // =========================================================
        // SAMBUNGKAN BUTANG MENU UTAMA KEPADA KOD JAVA
        // =========================================================
        AppCompatButton btnScanning = findViewById(R.id.btn_scanning);
        AppCompatButton btnObs = findViewById(R.id.btn_obs);
        AppCompatButton btnCabel = findViewById(R.id.btn_cabel);

        // 2. JALANKAN ANIMASI PADA KETIGA-TIGA BUTANG! (Ni yang tertinggal tadi)
        if (btnScanning != null) btnScanning.startAnimation(slideUp);
        if (btnObs != null) btnObs.startAnimation(slideUp);
        if (btnCabel != null) btnCabel.startAnimation(slideUp);

        // 1. Aksi klik Butang Scanning
        if (btnScanning != null) {
            btnScanning.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MenuActivity.this, ScanningActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });
        }

        // 2. Aksi klik Butang OBS Setting
        if (btnObs != null) {
            btnObs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MenuActivity.this, ObsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });
        }

        // 3. Aksi klik Butang Cabel Connect
        if (btnCabel != null) {
            btnCabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MenuActivity.this, CableActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });
        }

        // =========================================================
        // SAMBUNGKAN NAVIGASI BAR DI BAHAGIAN BAWAH
        // =========================================================
        LinearLayout navHome = findViewById(R.id.nav_home);
        LinearLayout navTask = findViewById(R.id.nav_task);
        LinearLayout navHelp = findViewById(R.id.nav_help);

        if (navHome != null) {
            navHome.setOnClickListener(v ->
                    Toast.makeText(MenuActivity.this, "Anda berada di Home!", Toast.LENGTH_SHORT).show()
            );
        }

        if (navTask != null) {
            navTask.setOnClickListener(v -> {
                Intent intent = new Intent(MenuActivity.this, ChallengeActivity.class);
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
    }
}
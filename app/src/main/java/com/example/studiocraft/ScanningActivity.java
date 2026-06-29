package com.example.studiocraft;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ScanningActivity extends AppCompatActivity {

    View[] lines = new View[10];
    Button btnInterlaced, btnProgressive;
    AppCompatButton btnInfoTutorial;
    TextView txtResult;
    boolean isInterlacedDone = false;
    boolean isProgressiveDone = false;
    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scanning);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.fade_slide_up);
        TextView tvTitle = findViewById(R.id.tv_title);
        if (tvTitle != null) {
            tvTitle.startAnimation(slideUp);
        }

        txtResult = findViewById(R.id.txtResult);
        btnInterlaced = findViewById(R.id.btn_sim_interlaced);
        btnProgressive = findViewById(R.id.btn_sim_progressive);
        btnInfoTutorial = findViewById(R.id.btn_info_tutorial);

        // Monitor Lines ID
        for (int i = 0; i < 10; i++) {
            int id = getResources().getIdentifier("line" + i, "id", getPackageName());
            lines[i] = findViewById(id);
        }

        // Interlaced Animation
        btnInterlaced.setOnClickListener(v -> {
            resetLines();
            btnInterlaced.setEnabled(false);
            btnProgressive.setEnabled(false);
            txtResult.setText("Loading Interlaced (1080i)...");
            txtResult.setTextColor(Color.parseColor("#ff27b5"));

            for (int i = 0; i < 10; i += 2) {
                lines[i].setBackgroundColor(Color.parseColor("#ff27b5"));
            }

            handler.postDelayed(() -> {
                for (int i = 1; i < 10; i += 2) {
                    lines[i].setBackgroundColor(Color.parseColor("#ff27b5"));
                }
                isInterlacedDone = true;
                btnInterlaced.setEnabled(true);
                btnProgressive.setEnabled(true);
                checkSelesai();
            }, 800);
        });

        // Progressive Animation
        btnProgressive.setOnClickListener(v -> {
            resetLines();
            btnInterlaced.setEnabled(false);
            btnProgressive.setEnabled(false);
            txtResult.setText("Loading Progressive (1080p)...");
            txtResult.setTextColor(Color.parseColor("#00e8ff"));

            for (int i = 0; i < 10; i++) {
                final int index = i;
                handler.postDelayed(() -> {
                    lines[index].setBackgroundColor(Color.parseColor("#00e8ff"));
                    if (index == 9) {
                        isProgressiveDone = true;
                        btnInterlaced.setEnabled(true);
                        btnProgressive.setEnabled(true);
                        checkSelesai();
                    }
                }, i * 100L);
            }
        });

        // Open Tutorial
        btnInfoTutorial.setOnClickListener(v -> {
            Intent intent = new Intent(ScanningActivity.this, ScanningTutorialActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Bottom Navigation
        LinearLayout navHome = findViewById(R.id.nav_home);
        LinearLayout navTask = findViewById(R.id.nav_task);
        LinearLayout navHelp = findViewById(R.id.nav_help);

        if (navHome != null) navHome.setOnClickListener(v -> { finish(); overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); });
        if (navTask != null) navTask.setOnClickListener(v -> { startActivity(new Intent(ScanningActivity.this, ChallengeActivity.class)); overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); });
        if (navHelp != null) navHelp.setOnClickListener(v -> { startActivity(new Intent(v.getContext(), HelpActivity.class)); overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); });
    }

    private void resetLines() {
        for (int i = 0; i < 10; i++) {
            lines[i].setBackgroundColor(Color.parseColor("#2E2E2E"));
        }
    }

    private void checkSelesai() {
        if (isInterlacedDone && isProgressiveDone) {
            txtResult.setText("Comparison Complete!\n1080i is the older technology.\n1080p is the new, smoother standard.");
            txtResult.setTextColor(Color.WHITE);
        }
    }
}
package com.example.studiocraft;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ScanningTutorialActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scanning_tutorial);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Animations
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.fade_slide_up);
        TextView tvTitle = findViewById(R.id.tv_title);
        LinearLayout layoutInterlaced = findViewById(R.id.layout_interlaced);
        LinearLayout layoutProgressive = findViewById(R.id.layout_progressive);

        if (tvTitle != null) tvTitle.startAnimation(slideUp);
        if (layoutInterlaced != null) layoutInterlaced.startAnimation(slideUp);
        if (layoutProgressive != null) layoutProgressive.startAnimation(slideUp);

        // Interactive Click Logic for Tutorial Cards
        TextView tvInterlacedExtra = findViewById(R.id.tv_interlaced_extra);
        TextView tvProgressiveExtra = findViewById(R.id.tv_progressive_extra);

        if (layoutInterlaced != null && tvInterlacedExtra != null) {
            layoutInterlaced.setOnClickListener(v -> {
                if (tvInterlacedExtra.getVisibility() == View.GONE) {
                    tvInterlacedExtra.setVisibility(View.VISIBLE);
                } else {
                    tvInterlacedExtra.setVisibility(View.GONE);
                }
            });
        }

        if (layoutProgressive != null && tvProgressiveExtra != null) {
            layoutProgressive.setOnClickListener(v -> {
                if (tvProgressiveExtra.getVisibility() == View.GONE) {
                    tvProgressiveExtra.setVisibility(View.VISIBLE);
                } else {
                    tvProgressiveExtra.setVisibility(View.GONE);
                }
            });
        }

        // Bottom Navigation
        LinearLayout navHome = findViewById(R.id.nav_home);
        LinearLayout navTask = findViewById(R.id.nav_task);
        LinearLayout navHelp = findViewById(R.id.nav_help);

        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                Intent intent = new Intent(ScanningTutorialActivity.this, MenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            });
        }
        if (navTask != null) {
            navTask.setOnClickListener(v -> {
                startActivity(new Intent(ScanningTutorialActivity.this, ChallengeActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            });
        }
        if (navHelp != null) {
            navHelp.setOnClickListener(v -> {
                startActivity(new Intent(v.getContext(), HelpActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            });
        }
    }
}
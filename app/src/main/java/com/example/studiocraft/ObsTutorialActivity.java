package com.example.studiocraft;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ObsTutorialActivity extends AppCompatActivity {

    private TextView kad444, kad422, kad420;
    private TextView target1, target2, target3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obs_tutorial);

        kad444 = findViewById(R.id.kad_444);
        kad422 = findViewById(R.id.kad_422);
        kad420 = findViewById(R.id.kad_420);

        target1 = findViewById(R.id.target_1);
        target2 = findViewById(R.id.target_2);
        target3 = findViewById(R.id.target_3);

        initDragSource(kad444, "4:4:4");
        initDragSource(kad422, "4:2:2");
        initDragSource(kad420, "4:2:0");

        initDropTarget(target1, "4:4:4");
        initDropTarget(target2, "4:2:2");
        initDropTarget(target3, "4:2:0");

        // =========================================================
        // SAMBUNGKAN NAVIGASI BAR DI BAHAGIAN BAWAH
        // =========================================================
        LinearLayout navHome = findViewById(R.id.nav_home);
        LinearLayout navTask = findViewById(R.id.nav_task);
        LinearLayout navHelp = findViewById(R.id.nav_help);

        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                Intent intent = new Intent(ObsTutorialActivity.this, MenuActivity.class);
                // Flag ini akan menutup semua skrin di atas MenuActivity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            });
        }

        if (navTask != null) {
            navTask.setOnClickListener(v -> {
                Intent intent = new Intent(ObsTutorialActivity.this, ChallengeActivity.class);
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

        // Set Animasi Naik
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.fade_slide_up);
        findViewById(R.id.tv_title).startAnimation(slideUp);
        // Mainkan animasi pada tajuk skrin tutorial
        TextView tvTitle = findViewById(R.id.tv_title);
        if (tvTitle != null) {
            tvTitle.startAnimation(slideUp);
        }
    }

    private void initDragSource(TextView tvCard, String value) {
        if (tvCard != null) {
            tvCard.setOnLongClickListener(v -> {
                ClipData.Item item = new ClipData.Item(value);
                ClipData dragData = new ClipData(value, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);

                View.DragShadowBuilder shadow = new View.DragShadowBuilder(tvCard);

                v.startDragAndDrop(dragData, shadow, null, 0);
                return true;
            });
        }
    }

    private void initDropTarget(TextView targetView, String correctAnswer) {
        if (targetView != null) {
            targetView.setOnDragListener((v, event) -> {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        return event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);

                    case DragEvent.ACTION_DRAG_ENTERED:
                        targetView.setBackgroundColor(Color.parseColor("#3300e8ff"));
                        return true;

                    case DragEvent.ACTION_DRAG_EXITED:
                        targetView.setBackgroundResource(R.drawable.target_dashed_border);
                        return true;

                    case DragEvent.ACTION_DROP:
                        ClipData data = event.getClipData();
                        String userAnswer = data.getItemAt(0).getText().toString();

                        if (userAnswer.equals(correctAnswer)) {
                            targetView.setText(userAnswer);
                            targetView.setTextColor(Color.parseColor("#00e8ff"));
                            targetView.setTextSize(16);
                            targetView.setBackgroundColor(Color.parseColor("#1B5E20"));
                            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();

                            removeCard(userAnswer);
                        } else {
                            targetView.setBackgroundResource(R.drawable.target_dashed_border);
                                Toast.makeText(this, "Wrong! Try matching somewhere else", Toast.LENGTH_SHORT).show();
                        }
                        return true;

                    case DragEvent.ACTION_DRAG_ENDED:
                        return true;
                }
                return false;
            });
        }
    }

    private void removeCard(String value) {
        if (value.equals("4:4:4") && kad444 != null) kad444.setVisibility(View.INVISIBLE);
        if (value.equals("4:2:2") && kad422 != null) kad422.setVisibility(View.INVISIBLE);
        if (value.equals("4:2:0") && kad420 != null) kad420.setVisibility(View.INVISIBLE);
    }
}
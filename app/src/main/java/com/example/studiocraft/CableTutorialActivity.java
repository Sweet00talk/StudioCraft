package com.example.studiocraft;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CableTutorialActivity extends AppCompatActivity {

    private CableLineView cableCanvas;
    private TextView tvFeedback;

    private FrameLayout[] leftBoxes;
    private TextView[] leftTexts;
    private TextView[] leftMarks;

    private final Set<String> matchedCableKeys = new HashSet<>();
    private final Set<Integer> completedLeftBoxIds = new HashSet<>();

    private float downRawX;
    private float downRawY;
    private float startTranslationX;
    private float startTranslationY;

    private int correctCount = 0;

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cable_tutorial);

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

        cableCanvas = findViewById(R.id.cable_canvas);
        tvFeedback = findViewById(R.id.tv_feedback);

        setupMatchGame();
        setupBottomNavigation();
    }

    private void setupMatchGame() {
        leftBoxes = new FrameLayout[]{
                findViewById(R.id.left_box_1),
                findViewById(R.id.left_box_2),
                findViewById(R.id.left_box_3),
                findViewById(R.id.left_box_4)
        };

        leftTexts = new TextView[]{
                findViewById(R.id.left_text_1),
                findViewById(R.id.left_text_2),
                findViewById(R.id.left_text_3),
                findViewById(R.id.left_text_4)
        };

        leftMarks = new TextView[]{
                findViewById(R.id.mark_left_1),
                findViewById(R.id.mark_left_2),
                findViewById(R.id.mark_left_3),
                findViewById(R.id.mark_left_4)
        };

        List<MatchItem> leftItems = new ArrayList<>();
        leftItems.add(new MatchItem("component", "Component\nVideo"));
        leftItems.add(new MatchItem("composite", "Composite\nVideo"));
        leftItems.add(new MatchItem("svideo", "S-Video"));
        leftItems.add(new MatchItem("vga", "VGA"));

        for (int i = 0; i < leftBoxes.length; i++) {
            MatchItem item = leftItems.get(i);

            leftTexts[i].setText(item.label);
            leftBoxes[i].setTag(item.key);
            leftBoxes[i].setBackgroundResource(R.drawable.match_left_box_bg);

            leftMarks[i].setVisibility(View.GONE);
            leftMarks[i].setText("");
        }

        setupDraggableCable(findViewById(R.id.cable_component), "component");
        setupDraggableCable(findViewById(R.id.cable_composite), "composite");
        setupDraggableCable(findViewById(R.id.cable_svideo), "svideo");
        setupDraggableCable(findViewById(R.id.cable_vga), "vga");
    }

    private void setupDraggableCable(ImageView cableView, String cableKey) {
        cableView.setTag(cableKey);

        cableView.setOnTouchListener((v, event) -> {
            if (matchedCableKeys.contains(cableKey)) {
                return true;
            }

            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    downRawX = event.getRawX();
                    downRawY = event.getRawY();

                    startTranslationX = v.getTranslationX();
                    startTranslationY = v.getTranslationY();

                    v.animate().cancel();

                    View rightSlot = (View) v.getParent();

                    rightSlot.setElevation(40f);
                    v.setElevation(50f);

                    PointF startPoint = getRightAnchor(rightSlot);
                    PointF dragPoint = screenPointToCanvas(event.getRawX(), event.getRawY());

                    cableCanvas.startTemporaryCable(startPoint, dragPoint);

                    if (tvFeedback != null) {
                        tvFeedback.setText("Drag the cable head to the correct name.");
                    }

                    return true;

                case MotionEvent.ACTION_MOVE:
                    float moveX = event.getRawX() - downRawX;
                    float moveY = event.getRawY() - downRawY;

                    v.setTranslationX(startTranslationX + moveX);
                    v.setTranslationY(startTranslationY + moveY);

                    PointF movePoint = screenPointToCanvas(event.getRawX(), event.getRawY());
                    cableCanvas.updateTemporaryCable(movePoint);

                    return true;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    cableCanvas.clearTemporaryCable();

                    View currentRightSlot = (View) v.getParent();
                    currentRightSlot.setElevation(0f);
                    v.setElevation(0f);

                    FrameLayout targetBox = findTouchedLeftBox(event.getRawX(), event.getRawY());

                    if (targetBox == null) {
                        resetCablePosition(v);

                        if (tvFeedback != null) {
                            tvFeedback.setText("Missed the box. Try again.");
                        }

                        return true;
                    }

                    String targetKey = String.valueOf(targetBox.getTag());

                    if (cableKey.equals(targetKey) && !completedLeftBoxIds.contains(targetBox.getId())) {
                        handleCorrectMatch(v, targetBox, cableKey);
                    } else {
                        handleWrongMatch(v, targetBox);
                    }

                    return true;
            }

            return false;
        });
    }

    private void handleCorrectMatch(View cableView, FrameLayout targetBox, String cableKey) {
        matchedCableKeys.add(cableKey);
        completedLeftBoxIds.add(targetBox.getId());

        View rightSlot = (View) cableView.getParent();

        cableView.animate()
                .translationX(0)
                .translationY(0)
                .setDuration(150)
                .start();

        targetBox.setBackgroundResource(R.drawable.match_box_correct);
        rightSlot.setBackgroundResource(R.drawable.match_box_correct);

        TextView mark = getMarkForBox(targetBox);
        if (mark != null) {
            mark.setText("✓");
            mark.setTextColor(Color.parseColor("#39FF14"));
            mark.setVisibility(View.VISIBLE);
        }

        PointF startPoint = getRightAnchor(rightSlot);
        PointF endPoint = getLeftAnchor(targetBox);

        cableCanvas.addCorrectCable(startPoint, endPoint);

        cableView.setEnabled(false);
        cableView.setAlpha(0.65f);

        correctCount++;

        if (correctCount == 4) {
            if (tvFeedback != null) {
                tvFeedback.setText("Complete! All cables matched correctly.");
            }

            Toast.makeText(this, "All cables connected!", Toast.LENGTH_SHORT).show();
        } else {
            if (tvFeedback != null) {
                tvFeedback.setText("Correct! Continue matching the cables.");
            }
        }
    }

    private void handleWrongMatch(View cableView, FrameLayout targetBox) {
        View rightSlot = (View) cableView.getParent();

        targetBox.setBackgroundResource(R.drawable.match_box_wrong);
        rightSlot.setBackgroundResource(R.drawable.match_box_wrong);

        TextView mark = getMarkForBox(targetBox);
        if (mark != null) {
            mark.setText("✕");
            mark.setTextColor(Color.parseColor("#FF304F"));
            mark.setVisibility(View.VISIBLE);
        }

        PointF startPoint = getRightAnchor(rightSlot);
        PointF endPoint = getLeftAnchor(targetBox);

        cableCanvas.showWrongCable(startPoint, endPoint);

        if (tvFeedback != null) {
            tvFeedback.setText("Wrong match. Try again.");
        }

        resetCablePosition(cableView);

        handler.postDelayed(() -> {
            if (!completedLeftBoxIds.contains(targetBox.getId())) {
                targetBox.setBackgroundResource(R.drawable.match_left_box_bg);

                if (mark != null) {
                    mark.setVisibility(View.GONE);
                }
            }

            String cableKey = String.valueOf(cableView.getTag());
            if (!matchedCableKeys.contains(cableKey)) {
                rightSlot.setBackgroundResource(R.drawable.match_right_box_bg);
            }

            cableCanvas.clearWrongCable();

        }, 650);
    }

    private void resetCablePosition(View cableView) {
        cableView.animate()
                .translationX(0)
                .translationY(0)
                .setDuration(220)
                .start();
    }

    private FrameLayout findTouchedLeftBox(float rawX, float rawY) {
        int[] location = new int[2];

        for (FrameLayout box : leftBoxes) {
            box.getLocationOnScreen(location);

            int left = location[0];
            int top = location[1];
            int right = left + box.getWidth();
            int bottom = top + box.getHeight();

            boolean insideX = rawX >= left && rawX <= right;
            boolean insideY = rawY >= top && rawY <= bottom;

            if (insideX && insideY) {
                return box;
            }
        }

        return null;
    }

    private TextView getMarkForBox(FrameLayout box) {
        for (int i = 0; i < leftBoxes.length; i++) {
            if (leftBoxes[i].getId() == box.getId()) {
                return leftMarks[i];
            }
        }

        return null;
    }

    private PointF getLeftAnchor(View view) {
        int[] viewLocation = new int[2];
        int[] canvasLocation = new int[2];

        view.getLocationOnScreen(viewLocation);
        cableCanvas.getLocationOnScreen(canvasLocation);

        float x = viewLocation[0] - canvasLocation[0] + view.getWidth();
        float y = viewLocation[1] - canvasLocation[1] + view.getHeight() / 2f;

        return new PointF(x, y);
    }

    private PointF getRightAnchor(View view) {
        int[] viewLocation = new int[2];
        int[] canvasLocation = new int[2];

        view.getLocationOnScreen(viewLocation);
        cableCanvas.getLocationOnScreen(canvasLocation);

        float x = viewLocation[0] - canvasLocation[0];
        float y = viewLocation[1] - canvasLocation[1] + view.getHeight() / 2f;

        return new PointF(x, y);
    }

    private PointF screenPointToCanvas(float rawX, float rawY) {
        int[] canvasLocation = new int[2];
        cableCanvas.getLocationOnScreen(canvasLocation);

        float x = rawX - canvasLocation[0];
        float y = rawY - canvasLocation[1];

        return new PointF(x, y);
    }

    private void setupBottomNavigation() {
        LinearLayout navHome = findViewById(R.id.nav_home);
        LinearLayout navTask = findViewById(R.id.nav_task);
        LinearLayout navHelp = findViewById(R.id.nav_help);

        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                Intent intent = new Intent(CableTutorialActivity.this, MenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            });
        }

        if (navTask != null) {
            navTask.setOnClickListener(v -> {
                Intent intent = new Intent(CableTutorialActivity.this, ChallengeActivity.class);
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

    private static class MatchItem {
        String key;
        String label;

        MatchItem(String key, String label) {
            this.key = key;
            this.label = label;
        }
    }
}
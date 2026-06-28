package com.example.studiocraft;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ChallengeActivity extends AppCompatActivity {

    // RANGKA DATA SOALAN (Boleh tukar teks soalan/jawapan dekat sini nanti)
    private final String[] questions = {
            "What is the main function of Progressive Scanning (1080p)?",
            "How many lines are updated in Interlaced Scanning at a time?",
            "What are the main advantages of the 4:4:4 Chroma Subsampling color setting?",
            "Which OBS setting is best for saving internet streaming bandwidth?",
            "Which cable is best for a gaming monitor with a refresh rate of 144Hz++?",
            "What type of signal does an HDMI cable carry?",
            "Which of the following is a bad effect of Interlaced Scanning?",
            "What does the abbreviation DP mean on a monitor cable?",
            "Does an HDMI cable carry audio signals along with video?",
            "What is the best option for smooth streaming video?"
    };

    private final String[][] options = {
            {"Overlapping images", "Show all lines at once", "Reduce colors", "Make video black and white"},
            {"All lines", "Half (Odd/Even)", "No lines", "Only one line"},
            {"Full & sharp colors", "Small files", "Fast internet", "Dimmable screen"},
            {"Chroma 4:4:4", "Chroma 4:2:2", "Chroma 4:2:0", "RGB Full Range"},
            {"VGA", "HDMI 1.0", "DisplayPort (DP)", "RCA cable"},
            {"Full Digital", "Legacy Analog", "Audio Signal Only", "Electric Power Signal"},
            {"Broken Screen", "Flickering/Distortion Effect", "Slow internet", "Battery drains quickly"},
            {"DisplayPort", "Digital Power", "Data Process", "Direct Post"},
            {"No, video only", "Yes, Audio + Video", "Only if using adapter", "Depending on graphics"},
            {"Use 4:4:4", "Use Progressive + 4:2:0", "Use Interlaced", "Close screen"}
    };


    private final int[] correctAnswers = {1, 1, 0, 2, 2, 0, 1, 0, 1, 1};

    private int currentQuestionIndex = 0;
    private int score = 0;

    private TextView tvQuestionNumber, tvQuestion;
    private AppCompatButton btnOpt1, btnOpt2, btnOpt3, btnOpt4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_challenge);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Set Animasi Naik
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.fade_slide_up);
        findViewById(R.id.tv_title).startAnimation(slideUp);
        findViewById(R.id.question_layout).startAnimation(slideUp);
        findViewById(R.id.options_layout).startAnimation(slideUp);

        // Inisialisasi Element UI
        tvQuestionNumber = findViewById(R.id.tv_question_number);
        tvQuestion = findViewById(R.id.tv_question);
        btnOpt1 = findViewById(R.id.btn_option1);
        btnOpt2 = findViewById(R.id.btn_option2);
        btnOpt3 = findViewById(R.id.btn_option3);
        btnOpt4 = findViewById(R.id.btn_option4);

        // Papar soalan pertama
        loadQuestion();

        // Tetapkan Aksi Klik untuk Pilihan Jawapan
        btnOpt1.setOnClickListener(v -> checkAnswer(0));
        btnOpt2.setOnClickListener(v -> checkAnswer(1));
        btnOpt3.setOnClickListener(v -> checkAnswer(2));
        btnOpt4.setOnClickListener(v -> checkAnswer(3));

        // =========================================================
        // SAMBUNGKAN NAVIGASI BAR DI BAHAGIAN BAWAH
        // =========================================================
        LinearLayout navHome = findViewById(R.id.nav_home);
        LinearLayout navTask = findViewById(R.id.nav_task);
        LinearLayout navHelp = findViewById(R.id.nav_help);

        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                Intent intent = new Intent(ChallengeActivity.this, MenuActivity.class);
                // Flag untuk pastikan dia bersih serta matikan skrin bertimbun di belakang
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            });
        }

        if (navTask != null) {
            navTask.setOnClickListener(v -> {
                Intent intent = new Intent(ChallengeActivity.this, ChallengeActivity.class);
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

    private void loadQuestion() {
        if (currentQuestionIndex < questions.length) {
            tvQuestionNumber.setText("Question: " + (currentQuestionIndex + 1) + "/" + questions.length);
            tvQuestion.setText(questions[currentQuestionIndex]);
            btnOpt1.setText(options[currentQuestionIndex][0]);
            btnOpt2.setText(options[currentQuestionIndex][1]);
            btnOpt3.setText(options[currentQuestionIndex][2]);
            btnOpt4.setText(options[currentQuestionIndex][3]);
        } else {
            showResultDialog();
        }
    }

    private void checkAnswer(int selectedOptionIndex) {
        if (selectedOptionIndex == correctAnswers[currentQuestionIndex]) {
            score++;
            Toast.makeText(this, "CORRECT! ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "WRONG! ", Toast.LENGTH_SHORT).show();
        }

        // Pergi ke soalan seterusnya
        currentQuestionIndex++;
        loadQuestion();
    }

    private void showResultDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CHALLENGE END!");
        builder.setMessage("Your Score: " + score + " / " + questions.length + "\n\nCongratulations on completing the StudioCraft challenge.!");
        builder.setCancelable(false);
        builder.setPositiveButton("BACK TO HOME", (dialog, which) -> {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
package com.example.studiocraft;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation; // IMPORT BARU
import android.view.animation.AnimationUtils; // IMPORT BARU
import android.widget.Button;
import android.widget.TextView; // IMPORT BARU

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // =========================================================
        // STEP 1: LOAD ANIMASI DARI CANVAS (fade_slide_up.xml)
        // =========================================================
        Animation animMasuk = AnimationUtils.loadAnimation(this, R.anim.fade_slide_up);

        // =========================================================
        // STEP 2: KENAL PASTI ELEMEN YANG NAK DIANIMASIKAN
        // =========================================================
        TextView tvCraft = findViewById(R.id.textView);
        // Pastikan anda menambah android:id="@+id/textViewStudio" pada teks "STUDIO" di XML anda
        TextView tvStudio = findViewById(R.id.textViewStudio);
        Button btnPlay = findViewById(R.id.btn_play);

        // =========================================================
        // STEP 3: JALANKAN ANIMASI
        // =========================================================
        if (tvCraft != null) tvCraft.startAnimation(animMasuk);
        if (tvStudio != null) tvStudio.startAnimation(animMasuk);
        if (btnPlay != null) btnPlay.startAnimation(animMasuk);

        // Fungsi klik butang PLAY untuk pindah page (Kekal sama)
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);

                // Animasi pertukaran halaman (Fade out skrin lama, Fade in skrin baru)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }
}
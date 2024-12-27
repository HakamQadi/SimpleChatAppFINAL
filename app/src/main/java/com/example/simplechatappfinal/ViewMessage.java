package com.example.simplechatappfinal;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.TextViewKt;

public class ViewMessage extends AppCompatActivity {

    MediaPlayer noteMP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_message);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        final TextView message = (TextView) findViewById(R.id.viewMessage);

        int userId = getIntent().getIntExtra("userId", -1);

        if (userId != -1) {
            //Play the note sound
            noteMP = new MediaPlayer();
            noteMP = MediaPlayer.create(this, R.raw.note);
            noteMP.start();

            DBHelper dbHelper = new DBHelper(this);
            Cursor cursor = dbHelper.getMessage(userId);

            if (cursor != null && cursor.moveToFirst()) {

                String body = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_BODY));
                message.setText(body);
                cursor.close();
            } else {
                message.setText(getString(R.string.message_not_found));
            }
        }
    }
}
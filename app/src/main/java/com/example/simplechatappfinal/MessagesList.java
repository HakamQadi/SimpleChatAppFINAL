package com.example.simplechatappfinal;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MessagesList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_messages_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final ListView messagesList = findViewById(R.id.messagesList);
        ArrayList<String> data = new ArrayList<>();
        final ArrayList<Integer> messageIds = new ArrayList<>();

        DBHelper dbHelper = new DBHelper(this);
        Cursor cursor = dbHelper.getAllChats();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_TITLE));

                data.add("User ID: " + userId + "\nTitle: " + title);

                messageIds.add(userId);
            } while (cursor.moveToNext());
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        messagesList.setAdapter(adapter);


        messagesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int selectedUserId = messageIds.get(i); // Get the ID of the clicked message
                Intent intent = new Intent(MessagesList.this, ViewMessage.class);
                intent.putExtra("userId", selectedUserId); // Pass the ID to ViewMessage
                startActivity(intent);
            }
        });



    }
}
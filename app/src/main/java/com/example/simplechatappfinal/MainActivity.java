package com.example.simplechatappfinal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    String userID;
    String selectedGender;
    String messageTitle;
    String messageBody;

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


        DBHelper db = new DBHelper(this);

        final ImageView image = (ImageView) findViewById(R.id.headerImg);
        final EditText userIdValue = (EditText) findViewById(R.id.idInput);
        final EditText messageTitleValue = (EditText) findViewById(R.id.messageTitleInput);
        final EditText messageBodyValue = (EditText) findViewById(R.id.messageBodyInput);
        final RadioGroup genderGroup = (RadioGroup) findViewById(R.id.genderGroup);
        final Button saveBtnSp = (Button) findViewById(R.id.saveBtnSP);
        final Button saveBtnSQL = (Button) findViewById(R.id.saveBtnSQL);

        genderGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1) {
                int genderImageRes = (checkedId == R.id.maleRadioBtn) ? R.drawable.male : R.drawable.female;
                image.setImageResource(genderImageRes);

                RadioButton selectedGenderBtn = findViewById(checkedId);
                selectedGender = selectedGenderBtn.getText().toString();
            }
        });

        saveBtnSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userID = userIdValue.getText().toString();
                messageTitle = messageTitleValue.getText().toString();
                messageBody = messageBodyValue.getText().toString();

                final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPref.edit();

                editor.putString("userId", userID);
                editor.putString("messageTitle", messageTitle);
                editor.putString("messageBody", messageBody);
                editor.apply();

                boolean fieldsStatus = Services.checkFields(genderGroup, userID, messageTitle, messageBody, MainActivity.this);
                if (fieldsStatus) {
                    return;
                }
                Services.showAlert(MainActivity.this, sharedPref);
                Services.resetFields(userIdValue, messageTitleValue, messageBodyValue, genderGroup, image, R.drawable.message);
            }
        });

        saveBtnSQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userID = userIdValue.getText().toString();
                messageTitle = messageTitleValue.getText().toString();
                messageBody = messageBodyValue.getText().toString();

                //Secret key to reset the DataBase
                if (Integer.parseInt(userID) == 2038531) {
                    db.resetDatabase();
                    Toast.makeText(MainActivity.this, "DataBase Reset Successfully", Toast.LENGTH_LONG).show();
                    Services.resetFields(userIdValue, messageTitleValue, messageBodyValue, genderGroup, image, R.drawable.message);
                    return;
                }

                boolean fieldsStatus = Services.checkFields(genderGroup, userID, messageTitle, messageBody, MainActivity.this);
                if (fieldsStatus) {
                    return;
                }

                boolean responseStatus = db.sendMessage(Integer.parseInt(userID), messageTitle, messageBody);
                if (responseStatus) {
                    Services.resetFields(userIdValue, messageTitleValue, messageBodyValue, genderGroup, image, R.drawable.message);
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this, MessagesList.class));
                } else {
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


}
package com.example.simplechatappfinal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Services {

    public static boolean checkFields(RadioGroup genderGroup, String userID, String messageTitle, String messageBody, Context context) {
        int selectedGenderID = genderGroup.getCheckedRadioButtonId(); // Check if a radio button in the RadioGroup is selected
        if (userID.isEmpty() || messageTitle.isEmpty() || messageBody.isEmpty() || selectedGenderID == -1) {
            Toast.makeText(context, "All Fields Must Be Not Empty", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public static void showAlert(Context context, SharedPreferences sharedPref) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        String savedUserID = sharedPref.getString("userId", "");
        String savedMessageTitle = sharedPref.getString("messageTitle", "");
        String savedMessageBody = sharedPref.getString("messageBody", "");

        builder.setTitle(context.getString(R.string.review_message_title, savedMessageTitle))
                .setMessage("User ID: " + savedUserID + "\n\nMessage Body: " + savedMessageBody)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void resetFields(EditText userIdValue, EditText messageTitleValue, EditText messageBodyValue, RadioGroup genderGroup, ImageView image, int defaultImageRes) {
        userIdValue.setText("");
        messageTitleValue.setText("");
        messageBodyValue.setText("");
        genderGroup.clearCheck();
        image.setImageResource(defaultImageRes);
    }
}

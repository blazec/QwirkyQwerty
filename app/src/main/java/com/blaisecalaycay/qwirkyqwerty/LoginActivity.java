package com.blaisecalaycay.qwirkyqwerty;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private TextView editTextUserID;
    private int userID;

    private RadioGroup radioGroupKeyboardType;

    private RadioButton radioButtonQwerty;
    private RadioButton radioButtonQwirky;
    private boolean qwerty;
    private boolean qwirky;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUserID = (TextView) findViewById(R.id.edit_text_user_id);

        radioButtonQwerty = (RadioButton) findViewById(R.id.radio_button_qwerty);
        radioButtonQwirky = (RadioButton) findViewById(R.id.radio_button_qwirky);

        radioButtonQwerty.setId(R.id.radio_button_qwerty);
        radioButtonQwirky.setId(R.id.radio_button_qwirky);

        radioGroupKeyboardType = (RadioGroup) findViewById(R.id.radio_group_keyboard_type);

    }


    public void start(View v) {
        String userIDString = editTextUserID.getText().toString();
        userID = userIDString.length() != 0? Integer.parseInt(userIDString) : 0;

        if (userID == 0) {
            Toast noUserNameToast = Toast.makeText(getApplicationContext(),
                    "Please enter a valid user ID", Toast.LENGTH_SHORT);
            noUserNameToast.show();
            return;
        }

        int checkedBtnId = radioGroupKeyboardType.getCheckedRadioButtonId();

        if (checkedBtnId == -1) {
            Toast selectFirstKeyboardToast = Toast.makeText(getApplicationContext(),
                                            "Please select first keyboard", Toast.LENGTH_SHORT);
            selectFirstKeyboardToast.show();
            return;
        }

        switch (checkedBtnId) {
            case R.id.radio_button_qwerty:
                qwerty = true;
                qwirky = false;
                break;
            case R.id.radio_button_qwirky:
                qwirky = true;
                qwerty = false;
                break;
        }

        Intent startIntent = new Intent(LoginActivity.this, PickSessionActivity.class);
        startIntent.putExtra("userID", userID);
        startIntent.putExtra("firstKeyboard", qwirky? "QWIRKY" : "QWERTY");
        startIntent.putExtra("currentKeyboard", qwirky? "QWIRKY" : "QWERTY");

        startActivity(startIntent,
                ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());

    }

}

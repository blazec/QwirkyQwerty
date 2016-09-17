package com.blaisecalaycay.qwirkyqwerty;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PickSessionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_session);

    }

    public void startSession(View v) {
        int session;
        final String TRIAL = "Trial";
        TextView textSession = (TextView) v;
        String sessionLabel = textSession.getText().toString();

        session = sessionLabel.equals(TRIAL)? 0 : Integer.parseInt(sessionLabel.split(" ")[1]);

        String[][] generatedSessionStrings = new SessionGenerator(session).generate();
        Intent startSessionIntent = new Intent(PickSessionActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("generatedSessionStrings", generatedSessionStrings);
        bundle.putInt("userID", getIntent().getIntExtra("userID", 0));
        bundle.putString("firstKeyboard", getIntent().getStringExtra("firstKeyboard"));
        bundle.putString("currentKeyboard", getIntent().getStringExtra("currentKeyboard"));
        bundle.putInt("session", session);
        startSessionIntent.putExtras(bundle);
        startActivity(startSessionIntent,
                ActivityOptions.makeSceneTransitionAnimation(PickSessionActivity.this).toBundle());
    }
}

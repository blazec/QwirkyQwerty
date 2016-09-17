package com.blaisecalaycay.qwirkyqwerty;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Date;

public class ResultsActivity extends AppCompatActivity {

    private static final String EMAIL_ADDRESS = "csc428group12@gmail.com";
    private static final String SUBJECT_DELIMITER = "_";
    private String emailSubject;

    private static final String QWERTY = "QWERTY";
    private static final String QWIRKY = "QWIRKY";

    private TextView textViewNumErrors;
    private TextView textViewElapsedTime;
    private TextView textViewFinalUserInput;
    private TextView textViewFTime;
    private TextView textViewTTime;
    private TextView textViewNumTimesF;
    private TextView textViewNumTimesT;

    private int userID;
    private String firstKeyboard;
    private String currentKeyboard;
    private String[][] generatedSessionStrings;
    private int session;

    private String[] csvContents = new String[8];
    private static final int CSV_KEYBOARD = 0;
    private static final int CSV_TIME = 1;
    private static final int CSV_ERRORS = 2;
    //private static final int CSV_UNCORRECTED_ERRORS = 2;
    private static final int CSV_NUMTIMES_T = 3;
    private static final int CSV_TRANSITION_TIME_T = 4;
    private static final int CSV_NUMTIMES_F = 5;
    private static final int CSV_TRANSITION_TIME_F = 6;
    private static final int CSV_FINAL_STRING = 7;
    private String csvString;
    //format:   time,errors,numtimes 't' was pressed,total transition time for 't',
    // numtimes 'f' was pressed,total transition time for 'f', final string

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        boolean uncountable = false;
        int uncorrectedErrors = 0;

//        textViewNumErrors = (TextView) findViewById(R.id.result_errors);
//        textViewElapsedTime = (TextView) findViewById(R.id.result_time);
//        textViewFinalUserInput = (TextView) findViewById(R.id.text_view_final_user_input);
//        textViewFTime = (TextView) findViewById(R.id.text_f_time);
//        textViewTTime = (TextView) findViewById(R.id.text_t_time);
//        textViewNumTimesF = (TextView) findViewById(R.id.text_num_fs);
//        textViewNumTimesT = (TextView) findViewById(R.id.text_num_ts);


        Intent resultsIntent = getIntent();
        int numErrors = resultsIntent.getIntExtra("numErrors", 0);
        double elapsedTime = (double) resultsIntent.getLongExtra("elapsedTime", 0);
        String finalUserInput = resultsIntent.getStringExtra("finalUserInput");
        String combinedPhrases = resultsIntent.getStringExtra("phrasesCombined");
        double fTimeTotal = (double) resultsIntent.getLongExtra("fTimeTotal", 0);
        double tTimeTotal = (double) resultsIntent.getLongExtra("tTimeTotal", 0);
        int numTimesF = resultsIntent.getIntExtra("numTimesF", 0);
        int numTimesT = resultsIntent.getIntExtra("numTimesT", 0);
        userID = resultsIntent.getIntExtra("userID", userID);
        session = resultsIntent.getIntExtra("session", 0);
        firstKeyboard = resultsIntent.getStringExtra("firstKeyboard");
        currentKeyboard = resultsIntent.getStringExtra("currentKeyboard");
        generatedSessionStrings = (String[][]) resultsIntent.getExtras().getSerializable("generatedSessionStrings");

        emailSubject = Integer.toString(userID) + "_" + session + "_" + "first" + ":" + firstKeyboard + ".csv";

        csvContents[CSV_KEYBOARD] = currentKeyboard;
        csvContents[CSV_TIME] = String.format("%.3f", elapsedTime / (double) 1000);
        csvContents[CSV_ERRORS] = Integer.toString(numErrors);
        csvContents[CSV_NUMTIMES_T] = Integer.toString(numTimesT);
        csvContents[CSV_TRANSITION_TIME_T] = String.format("%.3f", tTimeTotal / (double) 1000);
        csvContents[CSV_NUMTIMES_F] = Integer.toString(numTimesF);
        csvContents[CSV_TRANSITION_TIME_F] = String.format("%.3f", fTimeTotal / (double) 1000);
        csvContents[CSV_FINAL_STRING] = finalUserInput;

        csvString = resultsIntent.getStringExtra("csvContentsString");
        csvString = csvString == null? "" : csvString;

        for (int i = 0; i < csvContents.length; i++) {
            csvString += (i != CSV_FINAL_STRING? "" : "\"") + csvContents[i] + (i != CSV_FINAL_STRING? "," : "\"");
        }
        csvString.replaceAll("\\r?\\n", " ");
        csvString += "\n";

        //        textViewNumErrors.setText(Integer.toString(numErrors));
//        textViewElapsedTime.setText(String.format("%.3f", elapsedTime / (double) 1000)
//                                        + " seconds");
//        textViewFTime.setText(String.format("%.3f", fTimeTotal / (double) 1000)
//                + " seconds");
//        textViewTTime.setText(String.format("%.3f", tTimeTotal / (double) 1000)
//                + " seconds");
//        textViewNumTimesF.setText(Integer.toString(numTimesF));
//        textViewNumTimesT.setText(Integer.toString(numTimesT));
//
//        textViewFinalUserInput.setText(finalUserInput);


    }

    public void sendDataToEmail(View v) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        //intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {EMAIL_ADDRESS});
        intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        intent.putExtra(Intent.EXTRA_TEXT, csvString);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void continueTyping(View v) {
        currentKeyboard = currentKeyboard.equals(QWERTY)? QWIRKY : QWERTY;

        Intent continueTypingIntent = new Intent(ResultsActivity.this, MainActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("generatedSessionStrings", generatedSessionStrings);
        bundle.putInt("userID", userID);
        bundle.putString("firstKeyboard", firstKeyboard);
        bundle.putString("currentKeyboard", currentKeyboard);
        bundle.putInt("session", session);
        bundle.putString("csvContentsString", csvString);

        continueTypingIntent.putExtras(bundle);
        startActivity(continueTypingIntent, ActivityOptions.makeSceneTransitionAnimation(
                ResultsActivity.this).toBundle());
    }
}

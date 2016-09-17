package com.blaisecalaycay.qwirkyqwerty;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private int userID;
    private String currentKeyboard;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String QWIRKY = "QWIRKY";
    private static final String QWERTY = "QWERTY";

    private String[][] phraseArray;
    //phraseArray = [[hi how are you], [quick brown fox], [hello]]

    private TextView textViewCurrentKeyboard;

    private TextView textViewPhrase1;
    private TextView textViewPhrase2;

    private TextWatcher textWatcher;

    private int currentWordIdx = 0;
    private String currentWord;
    private String currentPhrase;
    private int currentPhraseIdx = -1;  //initialized this way for trackNextPhrase() to work
    private String nextPhrase;
    private int nextPhraseIdx = 0;  //initialized this way for trackNextPhrase() to work

    private int numPhrases = 0;

    private String[] wordArray;

    private SpannablePhrase spannablePhrase1;
    private SpannablePhrase spannablePhrase2;

    private EditText editTextUserInput;

    private TextView textViewNumErrors;
    private int numErrors;

    private String firstKeyboard;

    private String finalUserInput = "";

    private int progress = 0;
    private TextView textViewProgress;

    private Chronometer chronometer;


    //onTextListener VARIABLES
    private long fTime;
    private long fTimeTotal;
    private long tTime;
    private long tTimeTotal;
    private int numTimesT = 0;
    private int numTimesF = 0;
    private long startTime;
    private long endTime;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userID = getIntent().getIntExtra("userID", 0);
        firstKeyboard = getIntent().getStringExtra("firstKeyboard");
        currentKeyboard = getIntent().getStringExtra("currentKeyboard");

        phraseArray = (String[][]) getIntent().getExtras().getSerializable("generatedSessionStrings");

        //set numPhrases
        for (int k = 0; k < phraseArray.length - 1; k++) {
            numPhrases += phraseArray[k][0] != null? 1 : 0;
        }

        textViewCurrentKeyboard = (TextView) findViewById(R.id.text_current_keyboard);
        textViewCurrentKeyboard.setText(currentKeyboard);

        textViewPhrase1 = (TextView) findViewById(R.id.text_phrase1);
        textViewPhrase2 = (TextView) findViewById(R.id.text_phrase2);

        textViewProgress = (TextView) findViewById(R.id.text_progress);
        updateProgress(0);

        trackNextPhrase();

        textViewNumErrors = (TextView) findViewById(R.id.num_errors);
        numErrors = 0;

        chronometer = (Chronometer) findViewById(R.id.chronometer);

        editTextUserInput = (EditText) findViewById(R.id.user_input);

        textWatcher = initializeTextWatcher();
        editTextUserInput.addTextChangedListener(textWatcher);

    }

    public void showResults(View v) {
        showResults();
    }

    private void showResults() {

        endTime = endTime == 0? System.currentTimeMillis() : endTime;

        Bundle bundle = new Bundle();
        bundle.putSerializable("generatedSessionStrings", phraseArray);
        Intent resultsIntent = new Intent(MainActivity.this, ResultsActivity.class);
        bundle.putLong("elapsedTime", endTime - startTime);
        bundle.putInt("numErrors", numErrors);
        bundle.putString("finalUserInput", finalUserInput);
        bundle.putString("phrasesCombined", combinePhrases(phraseArray));
        bundle.putLong("fTimeTotal", fTimeTotal);
        bundle.putLong("tTimeTotal", tTimeTotal);
        bundle.putInt("numTimesF", numTimesF);
        bundle.putInt("numTimesT", numTimesT);
        bundle.putInt("userID", userID);
        bundle.putString("firstKeyboard", firstKeyboard);
        bundle.putString("currentKeyboard", currentKeyboard);
        bundle.putInt("session", getIntent().getIntExtra("session", 0));
        bundle.putString("csvContentsString", getIntent().getStringExtra("csvContentsString"));

        resultsIntent.putExtras(bundle);
        startActivity(resultsIntent,
                ActivityOptions.makeSceneTransitionAnimation(
                        MainActivity.this).toBundle());
    }

    public void resetToInitialState(View v) {
        resetToInitialState();
    }

    private void resetToInitialState() {
        spannablePhrase1.setSpannableStringAt(currentWordIdx, null);

        currentPhraseIdx = 0;
        nextPhraseIdx = 1;

        currentPhrase = phraseArray[currentPhraseIdx][0];
        nextPhrase = nextPhraseIdx < phraseArray.length - 1?
                phraseArray[nextPhraseIdx][0] : "";

        currentWordIdx = 0;
        wordArray = currentPhrase.split(" ");
        currentWord = wordArray[currentWordIdx];

        numErrors = 0;
        updateErrorValue(0);

        spannablePhrase1 = new SpannablePhrase(currentPhrase);
        spannablePhrase1.setSpannableStringAt(0, new ForegroundColorSpan(Color.BLUE));
        textViewPhrase1.setText(spannablePhrase1.toSpanned());

        spannablePhrase2 = new SpannablePhrase(nextPhrase);
        textViewPhrase2.setText(spannablePhrase2.toSpanned());

        finalUserInput = "";

        progress = 0;
        updateProgress(0);

        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());

        fTimeTotal = 0;
        tTimeTotal = 0;
        tTime = 0;
        fTime = 0;

        clearUserInput();
        editTextUserInput.removeTextChangedListener(textWatcher);
        textWatcher = initializeTextWatcher();
        editTextUserInput.addTextChangedListener(textWatcher);
    }

    private void trackNextPhrase() {

        currentPhraseIdx += 1;
        nextPhraseIdx += 1;

        currentPhrase = phraseArray[currentPhraseIdx][0];
        nextPhrase = nextPhraseIdx < phraseArray.length - 1?
                phraseArray[nextPhraseIdx][0] : " ";

        spannablePhrase1 = new SpannablePhrase(currentPhrase);
        spannablePhrase1.setSpannableStringAt(0, new ForegroundColorSpan(Color.BLUE));
        textViewPhrase1.setText(spannablePhrase1.toSpanned());

        spannablePhrase2 = new SpannablePhrase(nextPhrase != null? nextPhrase : " ");
        textViewPhrase2.setText(spannablePhrase2.toSpanned());

        wordArray = currentPhrase.split(" ");
        currentWordIdx = 0;
        currentWord = wordArray[currentWordIdx];

    }

    private void updateErrorValue(int num) {
        textViewNumErrors.setText(Integer.toString(num));
    }

    private void updateProgress(int num) {
        String progressString = Integer.toString(num)
                                + "/"
                                + Integer.toString(numPhrases);
        textViewProgress.setText(progressString);
    }

    private void clearUserInput() {
        editTextUserInput.setText("");
        editTextUserInput.setHint("");
    }

    private String getUserInput() {
        return editTextUserInput.getText().toString();
    }

    private void grayCurrentWord() {
        spannablePhrase1.setSpannableStringAt(currentWordIdx, null);
    }

    private void highlightCurrentWord() {
        spannablePhrase1.setSpannableStringAt(currentWordIdx,
                new ForegroundColorSpan(Color.BLUE));
    }

    private String combinePhrases(String[][] phraseArray) {
        //phraseArray = [[hi how are you], [quick brown fox], [hello]]

        String combinedPhrases = "";
        for (int i = 0; i < phraseArray.length; i++) {
            if (phraseArray[i][0] != null) {
                combinedPhrases += phraseArray[i][0] + (i != phraseArray.length - 1? "\n" : " ");
            }
        }
        return combinedPhrases;
    }

    private TextWatcher initializeTextWatcher() {
        return new TextWatcher() {

            boolean isInitialState = true;
            boolean isNextWord = false;

            char prevKey = '\u0000';  //null character


            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                /*
                This method is called to notify you that, within s, the count characters beginning
                at start are about to be replaced by new text with length after. It is an error
                to attempt to make changes to s from this callback.
                */

                //start timer
                if (currentWordIdx == 0 && isInitialState) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    startTime = System.currentTimeMillis();
                    isInitialState = false;
                    fTime = System.currentTimeMillis();
                    tTime = System.currentTimeMillis();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                /*
                  This method is called to notify you that, within s, the count characters beginning
                 at start have just replaced old text that had length before. It is an error to
                 attempt to make changes to s from this callback.
                 */

                int len = charSequence.length(); //necessary because count is working improperly
                //char typedChar = charSequence.charAt(len - 1);


                if (len <= currentWord.length()) {
                    if (before < len) {
                        // user did not press delete/backspace
                        if (prevKey == 't' || charSequence.charAt(len - 1) == 't'
                                || prevKey == 'T' || charSequence.charAt(len - 1) == 'T') {
                            tTimeTotal += System.currentTimeMillis() - tTime;
                            numTimesT += 1;
                        }
                        else if (prevKey == 'f' || charSequence.charAt(len - 1) == 'f'
                                || prevKey == 'F' || charSequence.charAt(len - 1) == 'F') {
                            fTimeTotal += System.currentTimeMillis() - fTime;
                            numTimesF += 1;
                        }

                        prevKey = charSequence.length() != 0? charSequence.charAt(len - 1) : prevKey;

                        if (charSequence.charAt(len - 1) != currentWord.charAt(len - 1)) {

                            if (charSequence.charAt(len - 1) == ' ') {
                                //user pressed space
                                numErrors += currentWord.length() - (charSequence.length() - 1);
                            }
                            else {
                                numErrors += 1;
                            }
                            updateErrorValue(numErrors);
                        }
                    }

                } else {
                    // here, the user input is longer than the word being compared

                    if (prevKey == 't' || charSequence.charAt(len - 1) == 't'
                            || prevKey == 'T' || charSequence.charAt(len - 1)== 'T') {
                        tTimeTotal += System.currentTimeMillis() - tTime;
                        numTimesT += 1;
                    }
                    else if (prevKey == 'f' || charSequence.charAt(len - 1) == 'f'
                            || prevKey == 'F' || charSequence.charAt(len - 1) == 'F') {
                        fTimeTotal += System.currentTimeMillis() - fTime;
                        numTimesF += 1;
                    }

                    prevKey = charSequence.length() != 0? charSequence.charAt(len - 1) : prevKey;

                    if (before < len && charSequence.charAt(len - 1) != ' ') {
                        // user did not press delete/backspace or user did not press space
                        numErrors += 1;
                        updateErrorValue(numErrors);
                    }
                }

                //prevKey = charSequence.charAt(len - 1);
                //prevKey = charSequence.charAt(charSequence.length() - 1);
                tTime = System.currentTimeMillis();
                fTime = System.currentTimeMillis();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                /*
                This method is called to notify you that, somewhere within s, the text has been
                changed. It is legitimate to make further changes to s from this callback, but be
                careful not to get yourself into an infinite loop, because any changes you make will
                cause this method to be called again recursively. (You are not told where the change
                took place because other afterTextChanged() methods may already have made other
                changes and invalidated the offsets. But if you need to know here, you can use
                setSpan(Object, int, int, int) in onTextChanged(CharSequence, int, int, int) to mark
                 your place and then look up from here where the span ended up.
                 */

                String userInput = getUserInput();

                if (userInput.equals("")) return;

                if (isNextWord) {
                    isNextWord = false;
                    return;
                }

                if (userInput.length() > 0 && userInput.charAt(userInput.length() - 1) == ' ') {
                    //user pressed space

                    if (currentWordIdx < wordArray.length - 1) {
                        grayCurrentWord();

                        clearUserInput();
                        currentWordIdx += 1;

                        highlightCurrentWord();

                        currentWord = wordArray[currentWordIdx];
                        isNextWord = true;

                        textViewPhrase1.setText(spannablePhrase1.toSpanned());

                        finalUserInput += editable.toString();

                    } else {
                        //check if there are more phrases
                        if (currentPhraseIdx < numPhrases - 1) {
                            clearUserInput();
                            trackNextPhrase();
                            progress += 1;
                            updateProgress(progress);
                            finalUserInput += editable.toString() + "\n";
                        } else {
                            //user has reached end of typing test
                            endTime = System.currentTimeMillis();
                            progress += 1;
                            updateProgress(progress + 1);
                            finalUserInput += editable.toString();

                            clearUserInput();

                            //show results
                            showResults();

                            //reset values
                            isNextWord = true; //flag that tells afterTextChanged not to trigger
                            resetToInitialState();
                            isInitialState = true;

                        }
                    }
                }
            }
        };
    }

}

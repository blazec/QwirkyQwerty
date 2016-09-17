package com.blaisecalaycay.qwirkyqwerty;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;

import java.util.Objects;

/**
 * Created by blaise on 2016-03-05.
 * A spannable phrase in this context is a phrase where each word is a distinct spannable string.
 * The class generates a spannable phrase based on the string given.
 */

public class SpannablePhrase {

    private SpannableString[] spannableStringArray;
    private Spanned spannablePhrase;
    private String mPhrase;
    private String[] wordArray;

    public SpannablePhrase(String phrase) {
        mPhrase = phrase;
        wordArray = phrase.split(" ");
        spannableStringArray = new SpannableString[wordArray.length];
        spannablePhrase = new SpannableString(" ");

        for (int i = 0; i < wordArray.length; i++) {
            SpannableString spannableWord = new SpannableString(wordArray[i] + " ");
            spannableStringArray[i] = spannableWord;
            spannablePhrase = (Spanned) TextUtils.concat(
                    spannablePhrase.toString().equals(" ")? "" : spannablePhrase, spannableWord);
        }
    }

    public SpannableString spannableStringAt(int i) {
        return spannableStringArray[i];
    }

    public Spanned toSpanned() {
        return spannablePhrase;
    }

    public void setSpannableStringAt(int i, Object what) {
        SpannableString oldSpannableString = spannableStringArray[i];
        SpannableString newSpannableString = new SpannableString(oldSpannableString.toString());
        newSpannableString.setSpan(what, 0,
                oldSpannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableStringArray[i] = newSpannableString;

        spannablePhrase = new SpannableString(" ");
        for (int j = 0; j < wordArray.length; j++) {
            spannablePhrase = (Spanned) TextUtils.concat(
                    spannablePhrase.toString().equals(" ")? "" : spannablePhrase,
                    spannableStringArray[j]);
        }

    }
}

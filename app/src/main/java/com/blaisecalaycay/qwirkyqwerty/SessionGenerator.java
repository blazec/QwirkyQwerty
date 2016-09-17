package com.blaisecalaycay.qwirkyqwerty;

import android.os.Environment;

import java.util.HashMap;

/**
 * Created by blaise on 2016-03-12.
 * SessionGenerator generates the phrase array to be used for a session
 */
public class SessionGenerator {

    private String trialPhrases = "the power of denial\n" +
            "you are not a jedi yet\n" +
            "yes you are very smart\n" +
            "soon we will return from the city\n" +
            "I am going to a music lesson\n" +
            "all together in one big pile\n" +
            "our fax number has changed";

    private String session1Phrases = "circumstances are unacceptable\n" +
            "listen to five hours of opera\n" +
            "in sharp contrast to your words\n" +
            "machinery is too complicated\n" +
            "the fire blazed all weekend\n" +
            "questioning the wisdom of the courts\n" +
            "they might find your comment offensive\n" +
            "do not lie in court or else\n" +
            "the first time he tried to swim\n" +
            "irregular verbs are the hardest to learn\n" +
            "you are a capitalist pig\n" +
            "a good stimulus deserves a good response\n" +
            "careless driving results in a fine\n" +
            "that sticker needs to be validated\n" +
            "the most beautiful sunset\n";

    private String session2Phrases = "my watch fell in the water\n" +
            "can I skate with sister today\n" +
            "if at first you do not succeed\n" +
            "universally understood to be wrong\n" +
            "rejection letters are discouraging\n" +
            "a steep learning curve in riding a unicycle\n" +
            "please try to be home before midnight\n" +
            "important news always seems to be late\n" +
            "the insulation is not working\n" +
            "rectangular objects have four sides\n" +
            "dinosaurs have been extinct for ages\n" +
            "the rationale behind the decision\n" +
            "drugs should be avoided\n" +
            "peering through a small hole\n" +
            "he watched in astonishment\n";

    private String session3Phrases = "just like it says on the can good\n" +
            "bring the offenders to justice\n" +
            "our housekeeper does a thorough job\n" +
            "two or three cups of coffee\n" +
            "give me one spoonful of coffee\n" +
            "everybody looses in custody battles\n" +
            "a thoroughly disgusting thing to say\n" +
            "a very traditional way to dress\n" +
            "did you see that spectacular explosion\n" +
            "longer than a football field\n" +
            "presidents drive expensive cars\n" +
            "be persistent to win a strike\n" +
            "the trains are always late\n" +
            "the bus was very crowded\n" +
            "suburbs are sprawling up everywhere ";

    private String session4Phrases = "nothing finer than discovering a treasure\n" +
            "the chancellor was very boring\n" +
            "dolphins leap high out of the water\n" +
            "people blow their horn a lot\n" +
            "every Saturday he folds the laundry\n" +
            "my favourite sport is racketball\n" +
            "the gun discharged by accident\n" +
            "that agreement is rife with problems\n" +
            "the assault took six months\n" +
            "the minimum amount of time\n" +
            "the treasury department is broke\n" +
            "I will put on my glasses\n" +
            "every apple from every tree\n" +
            "my mother makes good cookies\n" +
            "I want to hold your hand";

    private String session5Phrases = "correct your diction immediately\n" +
            "they love to yap about nothing\n" +
            "all good boys deserve fudge\n" +
            "we went grocery shopping\n" +
            "fish are jumping\n" +
            "my bank account is overdrawn\n" +
            "physics and chemistry are hard\n" +
            "wear a crown with many jewels\n" +
            "what a monkey sees a monkey will do \n" +
            "the cotton is high\n" +
            "the music is better than it sounds\n" +
            "my favorite subject is psychology\n" +
            "olympic athletes use drugs\n" +
            "if you come home late the doors are locked\n" +
            "an enlarged nose suggests you are a liar";

    private String session6Phrases = "that referendum asked a silly question\n" +
            "a tumor is OK provided it is benign\n" +
            "the treasurer must balance her books\n" +
            "rent is paid at the beginning of the month\n" +
            "our life expectancy has increased\n" +
            "construction makes traveling difficult\n" +
            "this system of taxation\n" +
            "freud wrote of the ego\n" +
            "I skimmed through your proposal\n" +
            "neither a borrower nor a lender be\n" +
            "please provide your date of birth\n" +
            "one heck of a question\n" +
            "a problem with the engine\n" +
            "we park in driveways\n" +
            "employee recruitment takes a lot of effort";

    private String session7Phrases = "travel at the speed of light" +
            "he underwent triple bypass surgery\n" +
            "handicapped persons need consideration\n" +
            "prepare for the exam in advance\n" +
            "mystery of the lost lagoon\n" +
            "victims deserve more redress\n" +
            "the largest of the five oceans\n" +
            "I watched blazing saddles\n" +
            "lydia wants to go home\n" +
            "he played a pimp in that movie\n" +
            "the accident scene is a shrine for fans\n" +
            "so you think you deserve a raise\n" +
            "nothing wrong with his style\n" +
            "that is a very nasty cut\n" +
            "I am going to a music lesson";

    private String session8Phrases;
    private String session9Phrases;
    private String session10Phrases;
    private String session11Phrases;
    private String session12Phrases;
    private String session13Phrases;
    private String session14Phrases;

    private HashMap<Integer, String> sessionHashMap = new HashMap<Integer, String>();
    private String[][] generatedSession;


    public SessionGenerator(int session) {

        sessionHashMap.put(0, trialPhrases);
        sessionHashMap.put(1, session1Phrases);
        sessionHashMap.put(2, session2Phrases);
        sessionHashMap.put(3, session3Phrases);
        sessionHashMap.put(4, session4Phrases);
        sessionHashMap.put(5, session5Phrases);
        sessionHashMap.put(6, session6Phrases);
        sessionHashMap.put(7, session7Phrases);

        String sessionString = sessionHashMap.get(session);

        generatedSession = new String[15][1];

        String[] sessionStringsLineBreakDelimited = sessionString.split("\\r?\\n");
        //sessionStringLineBreakDelimited = [hi how are you, quick brown fox, hello]
        //want: sessionStringsArray = [[hi how are you], [quick brown fox], [hello]]
        for (int i = 0; i < sessionStringsLineBreakDelimited.length; i++) {
            generatedSession[i] = new String[]{sessionStringsLineBreakDelimited[i]};
        }
    }

    public String[][] generate() {
        return generatedSession;
    }


}

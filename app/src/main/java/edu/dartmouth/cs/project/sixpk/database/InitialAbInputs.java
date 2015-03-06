package edu.dartmouth.cs.project.sixpk.database;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by koliver on 3/5/15.
 */
public class InitialAbInputs {
    //Muscle Groups
    public static final int RECTUS = 1;
    public static final int OBLIQUES = 2;
    public static final int TRANSVERSE = 3;

    //rectus exercises

    public static final String PLANK = "Plank";
    public static final String SITUP = "Sit-Up";
    public static final String PUSH = "Push Through";
    public static final String V = "V-Up";
    public static final String CRUNCH = "Crunches";
    public static final String LEG = "Leg Lift";
    public static final String TOE = "Toe Touch";

    public static String[] rectusArray = {PLANK, SITUP, PUSH, V, CRUNCH, LEG, TOE};

    //Obliques exercises

    public static final String RUSSIAN = "Russian Twist";
    public static final String STANDING = "Standing Oblique Crunch";
    public static final String BICYCLE = "Bicycle";
    public static final String ELBOW = "Elbow-to-knee Sit-Up";
    public static final String SIDE = "Side Plank";

    public static String[] obliqueArray = {RUSSIAN, STANDING, BICYCLE, ELBOW, SIDE};

    //Transverse exercises

    public static final String SCISSOR = "Scissor Kicks";
    public static final String ARM = "Arm Planks";
    public static final String SUPER = "Superman";
    public static final String REVERSE = "Reverse Curl Up";

    public static String[] tranverseArray = {SCISSOR, ARM, SUPER, REVERSE};

    //gifs rectus
    public static final String plGIF = "file:///android_asset/allison_plank.gif";
    public static final String sitGIF = "file:///android_asset/ian_sitUp.gif";
    public static final String pushGIF = "file:///android_asset/allison_pushThrough.gif";
    public static final String vGIF = "file:///android_asset/ian_vUP.gif";
    public static final String crunchGIF = "file:///android_asset/ian_crunch.gif";
    public static final String legGIF = "file:///android_asset/ian_legLift/gif";
    public static final String toeGIF = "file:///android_asset/ian_toeTouch.gif";

    public static String[] rectusGIFArray = {plGIF, sitGIF, pushGIF, vGIF, crunchGIF, legGIF, toeGIF};

    //gifs oblique
    public static final String russianGIF = "file:///android_asset/ian_russianTwist.gif";
    public static final String standingGIF = "file:///android_asset/allison_standingObliqueCrunch.gif";
    public static final String bicycleGIF = "file:///android_asset/ian_bicycle.gif";
    public static final String elbowGIF = "file:///android_asset/ian_elbowSitUp.gif";
    public static final String sideGIF = "file:///android_asset/allison_sidePlank.gif";

    public static String[] obliqueGIFArray = {russianGIF, standingGIF, bicycleGIF, elbowGIF, sideGIF};

    public static final String scissorGIF = "file:///android_asset/ian_scissorKicks.gif";
    public static final String armGIF = "file:///android_asset/allison_armPlank.gif";
    public static final String superGIF = "file:///android_asset/ian_superman.gif";
    public static final String reverseGIF = "file:///android_asset/allison_reverseCurlUp.gif";

    public static String[] transverseGIFArray = {scissorGIF, armGIF, superGIF, reverseGIF};






}

package com.dudar.colorfulmind;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.dudar.colorfulmind.alphabet.AlphabetHelpActivity;

import java.util.Random;

public class AlphabetActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener {

    TextView rainbowTextView, alphabetLetter0, alphabetLetter1, alphabetLetter2, alphabetLetter3,alphabetLetter4;
    TextView alphabetRLB0, alphabetRLB1, alphabetRLB2, alphabetRLB3,alphabetRLB4;
    Spinner gameTypeSpinner;
    SeekBar speedSeekBar;

    int spinnerPosition;
    int speedPosition;
    long speedInMillisec;

    static final String GAME_TYPE_SPINNER_KEY = "game_type_spinner";
    static final String GAME_SPEED_KEY = "speed_seekbar_position";

    static final int GAME_TYPE_ALPHABET = 0;
    static final int GAME_TYPE_NUMBERS = 1;
    static final int GAME_TYPE_RAINBOW = 2;

    static final int SPEED_SEEKBAR_MAX_VALUE = 4;
    static final int SPEED_SEEKBAR_SIZE = SPEED_SEEKBAR_MAX_VALUE + 1;

    static final int MAX_NUMBER_VALUE = 101;

    String[] colorsName;
    int[] colors;
    String[] hands;
    String[] letters;
    int randomColor, randomName;
    //int randomNumber;
    //String randomHand;
    //String randomLetter;

    TextView[] textViewsNumbersAndLetters;
    TextView[] textViewsRLBLetters;


    Random random;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alphabet);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initViews();
        setLettersTextViewsInvisible();

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        spinnerPosition = prefs.getInt(GAME_TYPE_SPINNER_KEY, GAME_TYPE_ALPHABET);
        speedPosition = prefs.getInt(GAME_SPEED_KEY, 0);
        Log.i("INFO: spinnerPosition", String.valueOf(spinnerPosition));
        Log.i("INFO: speedPosition", String.valueOf(speedPosition));

        gameTypeSpinner.setSelection(spinnerPosition);
        gameTypeSpinner.setOnItemSelectedListener(this);

        speedSeekBar.setProgress(speedPosition);
        speedSeekBar.setOnSeekBarChangeListener(this);
        speedSeekBar.setMax(SPEED_SEEKBAR_MAX_VALUE);

        Log.i("INFO: speedPosition 2", String.valueOf(speedPosition));

        Resources res = getResources();
        colorsName = res.getStringArray(R.array.colors_name_array);
        colors = res.getIntArray(R.array.colors_array);
        hands = res.getStringArray(R.array.hands_array);
        letters = res.getStringArray(R.array.letters_array);

        random = new Random();
        handler = new Handler();

        calculateSpeedInMillisec();
    }

    private void initViews() {
        rainbowTextView = (TextView) findViewById(R.id.textViewRainbow);
        rainbowTextView.setVisibility(View.INVISIBLE);

        alphabetLetter0 = (TextView) findViewById(R.id.textViewLetter0);
        alphabetLetter1 = (TextView) findViewById(R.id.textViewLetter1);
        alphabetLetter2 = (TextView) findViewById(R.id.textViewLetter2);
        alphabetLetter3 = (TextView) findViewById(R.id.textViewLetter3);
        alphabetLetter4 = (TextView) findViewById(R.id.textViewLetter4);

        textViewsNumbersAndLetters = new TextView[5];
        textViewsNumbersAndLetters[0] = alphabetLetter0;
        textViewsNumbersAndLetters[1] = alphabetLetter1;
        textViewsNumbersAndLetters[2] = alphabetLetter2;
        textViewsNumbersAndLetters[3] = alphabetLetter3;
        textViewsNumbersAndLetters[4] = alphabetLetter4;

        alphabetRLB0 = (TextView) findViewById(R.id.textViewRLB0);
        alphabetRLB1 = (TextView) findViewById(R.id.textViewRLB1);
        alphabetRLB2 = (TextView) findViewById(R.id.textViewRLB2);
        alphabetRLB3 = (TextView) findViewById(R.id.textViewRLB3);
        alphabetRLB4 = (TextView) findViewById(R.id.textViewRLB4);
        textViewsRLBLetters = new TextView[5];
        textViewsRLBLetters[0] = alphabetRLB0;
        textViewsRLBLetters[1] = alphabetRLB1;
        textViewsRLBLetters[2] = alphabetRLB2;
        textViewsRLBLetters[3] = alphabetRLB3;
        textViewsRLBLetters[4] = alphabetRLB4;

        gameTypeSpinner = (Spinner) findViewById(R.id.spinner);
        speedSeekBar = (SeekBar) findViewById(R.id.speedSeekBar);
    }

    private void setLettersTextViewsInvisible() {
        alphabetLetter0.setVisibility(View.INVISIBLE);
        alphabetLetter1.setVisibility(View.INVISIBLE);
        alphabetLetter2.setVisibility(View.INVISIBLE);
        alphabetLetter3.setVisibility(View.INVISIBLE);
        alphabetLetter4.setVisibility(View.INVISIBLE);

        alphabetRLB0.setVisibility(View.INVISIBLE);
        alphabetRLB1.setVisibility(View.INVISIBLE);
        alphabetRLB2.setVisibility(View.INVISIBLE);
        alphabetRLB3.setVisibility(View.INVISIBLE);
        alphabetRLB4.setVisibility(View.INVISIBLE);
    }

    Runnable scheduledTaskRainbow = new Runnable() {
        @Override
        public void run() {
            changeRainbowText();
            handler.postDelayed(this, speedInMillisec);
        }
    };

    private void changeRainbowText() {
        setLettersTextViewsInvisible();

        randomName = random.nextInt(colorsName.length);
        randomColor = random.nextInt(colors.length);

        rainbowTextView.setText(colorsName[randomName]);
        rainbowTextView.setTextColor(colors[randomColor]);

    }


    Runnable scheduledTaskAlphabet = new Runnable() {
        @Override
        public void run() {
            Log.i("task in alphabet", "millis passed");
            handler.postDelayed(this, speedInMillisec);
            changeAlphabetLetters();
        }
    };

    private void changeAlphabetLetters() {
        String randomLetter, randomHand;

        setLettersTextViewsInvisible();

        randomLetter = letters[random.nextInt(letters.length)];
        randomHand = hands[random.nextInt(hands.length)];
        showLettersNumbers(randomLetter,randomHand);

    }

    Runnable scheduledTaskNumbers = new Runnable() {
        @Override
        public void run() {
            Log.i("task in numbers", "millis passed");
            handler.postDelayed(this, speedInMillisec);
            changeNumbers();
        }
    };

    private void changeNumbers() {
        int randomNumber;
        String randomHand;

        setLettersTextViewsInvisible();

        randomNumber = random.nextInt(MAX_NUMBER_VALUE);
        randomHand = hands[random.nextInt(hands.length)];
        //Log.i("randon number", String.valueOf(randomNumber));
        //Log.i("randon hand", randomHand);

        showLettersNumbers(String.valueOf(randomNumber),randomHand);

    }

    private void showLettersNumbers(String topTextViewValue, String bottomTextViewValue) {
        int randTag = random.nextInt(5);
        textViewsNumbersAndLetters[randTag].setVisibility(View.VISIBLE);
        textViewsNumbersAndLetters[randTag].setText(topTextViewValue);
        textViewsNumbersAndLetters[randTag].setTextColor(colors[random.nextInt(colors.length)]);
        textViewsRLBLetters[randTag].setVisibility(View.VISIBLE);
        textViewsRLBLetters[randTag].setText(bottomTextViewValue);
        textViewsRLBLetters[randTag].setTextColor(colors[random.nextInt(colors.length)]);
    }

    private void startRepeatingTask(int gameType) {
        switch (gameType) {
            case GAME_TYPE_ALPHABET:
                scheduledTaskAlphabet.run();
                break;
            case GAME_TYPE_NUMBERS:
                scheduledTaskNumbers.run();
                break;
            case GAME_TYPE_RAINBOW:
                scheduledTaskRainbow.run();
                break;
        }
        //scheduledTask.run();
    }

    private void stopRepeatingTask(int gameType) {
        switch (gameType) {
            case GAME_TYPE_ALPHABET:
                handler.removeCallbacks(scheduledTaskAlphabet);
                break;
            case GAME_TYPE_NUMBERS:
                handler.removeCallbacks(scheduledTaskNumbers);
                break;
            case GAME_TYPE_RAINBOW:
                handler.removeCallbacks(scheduledTaskRainbow);
                break;
        }
        //handler.removeCallbacks(scheduledTask);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        spinnerPosition = position;

        // On selecting a spinner item
        //String item = parent.getItemAtPosition(position).toString();

        switch (spinnerPosition) {
            case GAME_TYPE_ALPHABET:
                startAlphabetGame();
                break;
            case GAME_TYPE_NUMBERS:
                startNumbersGame();
                break;
            case GAME_TYPE_RAINBOW:
                startRainbowGame();
                break;
        }
    }

    private void startAlphabetGame() {
        stopRepeatingTask(GAME_TYPE_NUMBERS);
        stopRepeatingTask(GAME_TYPE_RAINBOW);
        startRepeatingTask(GAME_TYPE_ALPHABET);

        Log.i("alphabet", "started");
        rainbowTextView.setVisibility(View.INVISIBLE);
    }

    private void startNumbersGame() {
        stopRepeatingTask(GAME_TYPE_ALPHABET);
        stopRepeatingTask(GAME_TYPE_RAINBOW);
        startRepeatingTask(GAME_TYPE_NUMBERS);

        Log.i("numbers", "started");
        rainbowTextView.setVisibility(View.INVISIBLE);
    }

    private void startRainbowGame() {
        stopRepeatingTask(GAME_TYPE_NUMBERS);
        stopRepeatingTask(GAME_TYPE_ALPHABET);
        startRepeatingTask(GAME_TYPE_RAINBOW);

        Log.i("rainbow", "started");
        rainbowTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int position, boolean b) {
        speedPosition = position;
        Log.i("Speed position", String.valueOf(speedPosition));

        calculateSpeedInMillisec();
        Log.i("Speed ", String.valueOf(speedInMillisec));
    }

    private void calculateSpeedInMillisec() {
        double speedDouble = (double) (SPEED_SEEKBAR_SIZE - speedPosition) / 2;
        Log.i("Speed double", String.valueOf(speedDouble));

        speedInMillisec = (long) (speedDouble * 1000);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(GAME_TYPE_SPINNER_KEY, spinnerPosition);
        editor.putInt(GAME_SPEED_KEY, speedPosition);
        Log.i("Speed position onstop", String.valueOf(speedPosition));
        editor.apply();

        handler.removeCallbacks(scheduledTaskRainbow);
        handler.removeCallbacks(scheduledTaskAlphabet);
        handler.removeCallbacks(scheduledTaskNumbers);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_alphabet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_help:
                startActivity(new Intent(this, AlphabetHelpActivity.class));
                return true;
            default:
                return true;
        }
    }
}

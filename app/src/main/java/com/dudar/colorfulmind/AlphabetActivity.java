package com.dudar.colorfulmind;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Random;

public class AlphabetActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener {

    TextView rainbowTextView, alphabetLetter0, alphabetLetter1, alphabetLetter2, alphabetLetter3;
    TextView alphabetRLB1, alphabetRLB2, alphabetRLB3, alphabetRLB4;
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

    String[] colorsName;
    int[] colors;
    String[] hands;
    int randomColor, randomName;

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

        random = new Random();
        handler = new Handler();

        calculateSpeedInMillisec();
    }

    private void initViews() {
        rainbowTextView = (TextView) findViewById(R.id.textViewRainbow);
        rainbowTextView.setVisibility(View.INVISIBLE);

        alphabetLetter0 = (TextView) findViewById(R.id.textViewLetter0);
        alphabetLetter0.setVisibility(View.INVISIBLE);
        alphabetLetter1 = (TextView) findViewById(R.id.textViewLetter1);
        alphabetLetter1.setVisibility(View.INVISIBLE);
        alphabetLetter2 = (TextView) findViewById(R.id.textViewLetter2);
        alphabetLetter2.setVisibility(View.INVISIBLE);
        alphabetLetter3 = (TextView) findViewById(R.id.textViewLetter3);
        alphabetLetter3.setVisibility(View.INVISIBLE);

        alphabetRLB1 = (TextView) findViewById(R.id.textViewRLB1);
        alphabetRLB1.setVisibility(View.INVISIBLE);
        alphabetRLB2 = (TextView) findViewById(R.id.textViewRLB2);
        alphabetRLB2.setVisibility(View.INVISIBLE);
        alphabetRLB3 = (TextView) findViewById(R.id.textViewRLB3);
        alphabetRLB3.setVisibility(View.INVISIBLE);
        alphabetRLB4 = (TextView) findViewById(R.id.textViewRLB4);
        alphabetRLB4.setVisibility(View.INVISIBLE);


        gameTypeSpinner = (Spinner) findViewById(R.id.spinner);
        speedSeekBar = (SeekBar) findViewById(R.id.speedSeekBar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                return true;
        }
    }

    Runnable scheduledTaskRainbow = new Runnable() {
        @Override
        public void run() {
            changeRainbowText();
            handler.postDelayed(this, speedInMillisec);
        }
    };

    private void changeRainbowText() {
        //Log.i("runnable", "rainbow");

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
        double speedDouble =(double) (SPEED_SEEKBAR_SIZE - speedPosition) / 2;
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

}

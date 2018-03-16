package com.dudar.colorfulmind;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Random;

public class RainbowActivity extends AppCompatActivity {

    String[] colorsName;
    int [] colors;
    TextView rainbowTxt;

    Random random;
    int randomColor, randomName;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rainbow);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Resources res = getResources();
        colorsName = res.getStringArray(R.array.colors_name_array);
        colors = res.getIntArray(R.array.colors_array);

        rainbowTxt = (TextView) findViewById(R.id.rainbow_text);
        //rainbowTxt.setText(colorsName[5]);
        //rainbowTxt.setTextColor(colors[2]);

        random = new Random();

        handler= new Handler();
        startRepeatingTask();

        //startExercise();
        //changeRainbowText();
    }
    Runnable scheduledTask= new Runnable() {
        @Override
        public void run() {
            changeRainbowText();
            handler.postDelayed(scheduledTask, 1500);
        }
    };

    private void startRepeatingTask() {
        scheduledTask.run();
    }


    private void startExercise() {
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                changeRainbowText();
            }
        },1500);*/
    }

    private void changeRainbowText() {
        Log.i("MyTest","inside runnable");

        randomName = random.nextInt(colorsName.length);
        randomColor = random.nextInt(colors.length);

        rainbowTxt.setText(colorsName[randomName]);
        rainbowTxt.setTextColor(colors[randomColor]);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    private void stopRepeatingTask() {
        handler.removeCallbacks(scheduledTask);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                return true;
        }
    }
}

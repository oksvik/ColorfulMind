package com.dudar.colorfulmind.colorlogic;

import android.util.Log;

import java.util.Random;

public class ColorLogicPresenter implements ColorLogicContract.Presenter {

    private ColorLogicContract.View view;
    private ColorLogicItem attemptColors, secretColors;

    private ColorLogicHistory history;

    public ColorLogicPresenter(ColorLogicContract.View view) {
        this.view = view;
        attemptColors = new ColorLogicItem();
        secretColors = new ColorLogicItem();

        history = ColorLogicHistory.getHistoryInstance();
    }

    @Override
    public void setSecretColors(int[] baseColors, int numberOfColors, boolean isDuplicationAllowed) {
        Random r = new Random();
        int nextRandom, randomColor;
        int[] colors = new int[4];

        for (int i = 0; i < 4; i++) {
            nextRandom = r.nextInt(numberOfColors);
            randomColor = baseColors[nextRandom];
            if (!isDuplicationAllowed)
                while (isInArray(colors, randomColor)) {
                    nextRandom = r.nextInt(numberOfColors);
                    randomColor = baseColors[nextRandom];
                }
            colors[i] = randomColor;
        }
        secretColors.setColors(colors[0], colors[1], colors[2], colors[3]);
        Log.i("secret colors", secretColors.toString());
    }

    private boolean isInArray(int[] colors, int randomColor) {
        for (int color : colors)
            if (color == randomColor)
                return true;
        return false;
    }

    @Override
    public void makePlayerStep(int color1, int color2, int color3, int color4) {

        attemptColors.setColor1(color1);
        attemptColors.setColor2(color2);
        attemptColors.setColor3(color3);
        attemptColors.setColor4(color4);

        Log.i("attemptColors", attemptColors.toString());

        int colBull, colCow;

        colBull = secretColors.countBulls(attemptColors);
        Log.i("counted bulls: ", String.valueOf(colBull));

        colCow = secretColors.countCows(attemptColors);
        Log.i("counted cows:", String.valueOf(colCow));

        addAttemptInGameHistory(attemptColors, colBull, colCow);

        view.updateHistoryView(colBull);
    }

    private void addAttemptInGameHistory(ColorLogicItem attemptColors, int colBull, int colCow) {
        ColorLogicHistoryItem historyItem = new ColorLogicHistoryItem(attemptColors, colBull, colCow);
        history.addHistoryItem(historyItem);

    }

    @Override
    public ColorLogicItem getSecretColors() {
        return secretColors;
    }

    @Override
    public void copySecretColors(int[] restoredColors) {
        secretColors.setColors(restoredColors[0],restoredColors[1],restoredColors[2],restoredColors[3]);
    }


}

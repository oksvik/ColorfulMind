package com.dudar.colorfulmind.colorlogic;

public class ColorLogicHistoryItem {
    private int color1, color2, color3, color4;
    private int blackBull, whiteCow;

    public ColorLogicHistoryItem(ColorLogicItem attemptColors, int colBull, int colCow) {
        color1 = attemptColors.getColor1();
        color2 = attemptColors.getColor2();
        color3 = attemptColors.getColor3();
        color4 = attemptColors.getColor4();

        blackBull = colBull;
        whiteCow = colCow;
    }

    public int getColor1() {
        return color1;
    }

    public int getColor2() {
        return color2;
    }

    public int getColor3() {
        return color3;
    }

    public int getColor4() {
        return color4;
    }

    public int getBlackBullNumber() {
        return blackBull;
    }

    public int getWhiteCowNumber() {
        return whiteCow;
    }
}

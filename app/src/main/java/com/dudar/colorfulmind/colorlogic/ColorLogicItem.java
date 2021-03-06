package com.dudar.colorfulmind.colorlogic;

import java.util.ArrayList;

public class ColorLogicItem {
    private int color1, color2, color3, color4;

    ColorLogicItem() {
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

    void setColors(int color1, int color2, int color3, int color4) {
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.color4 = color4;
    }

    void setColor1(int color1) {
        this.color1 = color1;
    }

    void setColor2(int color2) {
        this.color2 = color2;
    }

    void setColor3(int color3) {
        this.color3 = color3;
    }

    void setColor4(int color4) {
        this.color4 = color4;
    }

    @Override
    public String toString() {
        return "ColorLogicItem{" +
                "color1=" + color1 +
                ", color2=" + color2 +
                ", color3=" + color3 +
                ", color4=" + color4 +
                '}';
    }

    int countBulls(ColorLogicItem attemptColors) {
        int bulls = 0;
        if (color1 == attemptColors.color1)
            bulls++;
        if (color2 == attemptColors.color2)
            bulls++;
        if (color3 == attemptColors.color3)
            bulls++;
        if (color4 == attemptColors.color4)
            bulls++;

        return bulls;
    }

    int countCows(ColorLogicItem attemptColors) {
        int cows = 0;
        ArrayList<Integer> arColor = new ArrayList<>();
        ArrayList<Integer> arAttempt = new ArrayList<>();

        if (color1 != attemptColors.color1) {
            arColor.add(color1);
            arAttempt.add(attemptColors.color1);
        }
        if (color2 != attemptColors.color2) {
            arColor.add(color2);
            arAttempt.add(attemptColors.color2);
        }
        if (color3 != attemptColors.color3) {
            arColor.add(color3);
            arAttempt.add(attemptColors.color3);
        }
        if (color4 != attemptColors.color4) {
            arColor.add(color4);
            arAttempt.add(attemptColors.color4);
        }

        for (Integer c : arColor) {
            if (arAttempt.contains(c)) {
                arAttempt.remove(c);
                cows++;
            }
        }

        return cows;
    }
}

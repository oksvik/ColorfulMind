package com.dudar.colorfulmind.colorlogic;

public interface ColorLogicContract {
    interface View{
        void updateHistoryView(int colBull);
    }

    interface Presenter {
        void makePlayerStep(int color1, int color2, int color3, int color4);
        void setSecretColors(int[] baseColors, int numberOfColors, boolean isDuplicationAllowed);
        ColorLogicItem getSecretColors();

        void copySecretColors(int[] restoredColors);
    }
}

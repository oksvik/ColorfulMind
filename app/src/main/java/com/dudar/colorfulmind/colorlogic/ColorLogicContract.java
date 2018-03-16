package com.dudar.colorfulmind.colorlogic;

/**
 * Created by dudar on 19.02.2018.
 */

public interface ColorLogicContract {
    interface View{
        void updateHistoryView(int colBull);
    }

    interface Presenter {
        void makePlayerStep(int color1, int color2, int color3, int color4);
        void setSecretColors(int[] baseColors, int numberOfColors, boolean isDuplecationAllowed);
        ColorLogicItem getSecretColors();

    }
}

package com.dudar.colorfulmind.colorlogic;

import java.util.ArrayList;

/**
 * Created by dudar on 23.01.2018.
 */

public class GameLogUtils {

    private static ArrayList<ColorLogicItem> logOfColors = new ArrayList<ColorLogicItem>();

    public static ArrayList<ColorLogicItem> getLogOfColors() {
        return logOfColors;
    }

    public static void setLogOfColors(ArrayList<ColorLogicItem> logOfColors) {
        GameLogUtils.logOfColors = logOfColors;
    }
    public void addLogOfColorsItem(int color){

    }
}

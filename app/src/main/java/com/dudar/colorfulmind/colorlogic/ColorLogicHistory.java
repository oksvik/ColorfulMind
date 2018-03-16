package com.dudar.colorfulmind.colorlogic;

import java.util.ArrayList;

/**
 * Created by dudar on 23.02.2018.
 */

public class ColorLogicHistory {
    private static ColorLogicHistory historyInstance = null;

    private ArrayList<ColorLogicHistoryItem> itemsOfGameHistory;

    private ColorLogicHistory(){
        itemsOfGameHistory = new ArrayList<ColorLogicHistoryItem>();
    }

    public static ColorLogicHistory getHistoryInstance(){
        if (historyInstance == null)
            historyInstance = new ColorLogicHistory();
        return historyInstance;

    }

    public ArrayList<ColorLogicHistoryItem> getHistoryItems(){
        return itemsOfGameHistory;
    }

    public void addHistoryItem (ColorLogicHistoryItem item){
        itemsOfGameHistory.add(item);
    }

    public void deleteHistory(){
        itemsOfGameHistory.clear();
    }
}

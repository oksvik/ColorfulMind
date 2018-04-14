package com.dudar.colorfulmind.colorlogic;

import java.util.ArrayList;

public class ColorLogicHistory {
    private static ColorLogicHistory historyInstance = null;

    private ArrayList<ColorLogicHistoryItem> itemsOfGameHistory;

    private ColorLogicHistory(){
        itemsOfGameHistory = new ArrayList<>();
    }

    public static ColorLogicHistory getHistoryInstance(){
        if (historyInstance == null)
            historyInstance = new ColorLogicHistory();
        return historyInstance;

    }

    public ArrayList<ColorLogicHistoryItem> getHistoryItems(){
        return itemsOfGameHistory;
    }

    void addHistoryItem (ColorLogicHistoryItem item){
        itemsOfGameHistory.add(item);
    }

    public void deleteHistory(){
        itemsOfGameHistory.clear();
    }

    public void copyHistory(ArrayList<ColorLogicHistoryItem> recoveredHistory) {
        itemsOfGameHistory = recoveredHistory;
    }
}

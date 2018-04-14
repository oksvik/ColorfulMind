package com.dudar.colorfulmind.colorlogic;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class ColorLogicHistoryItem implements Parcelable{
    private int color1, color2, color3, color4;
    private int blackBull, whiteCow;

    ColorLogicHistoryItem(ColorLogicItem attemptColors, int colBull, int colCow) {
        color1 = attemptColors.getColor1();
        color2 = attemptColors.getColor2();
        color3 = attemptColors.getColor3();
        color4 = attemptColors.getColor4();

        blackBull = colBull;
        whiteCow = colCow;
        Log.i("create history item","constructor");
    }

    private ColorLogicHistoryItem(Parcel in){
        int[] arrayOfData = new int[6];
        in.readIntArray(arrayOfData);
        color1 = arrayOfData[0];
        color2 = arrayOfData[1];
        color3 = arrayOfData[2];
        color4 = arrayOfData[3];
        blackBull = arrayOfData[4];
        whiteCow = arrayOfData[5];
    }

    int getColor1() {
        return color1;
    }

    int getColor2() {
        return color2;
    }

    int getColor3() {
        return color3;
    }

    int getColor4() {
        return color4;
    }

    int getBlackBullNumber() {
        return blackBull;
    }

    int getWhiteCowNumber() {
        return whiteCow;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        int[] arrayToParcel = {color1,color2,color3,color4,blackBull,whiteCow};
        parcel.writeIntArray(arrayToParcel);
        Log.i("write to parcel",arrayToParcel.toString());
    }

    public static final Parcelable.Creator<ColorLogicHistoryItem> CREATOR = new Parcelable.Creator<ColorLogicHistoryItem>(){
        public ColorLogicHistoryItem createFromParcel(Parcel in) {
            return new ColorLogicHistoryItem(in);
        }

        public ColorLogicHistoryItem[] newArray(int size) {
            return new ColorLogicHistoryItem[size];
        }
    };

    @Override
    public String toString() {
        return "ColorLogicHistoryItem{" +
                "color1=" + color1 +
                ", color2=" + color2 +
                ", color3=" + color3 +
                ", color4=" + color4 +
                ", blackBull=" + blackBull +
                ", whiteCow=" + whiteCow +
                '}';
    }
}

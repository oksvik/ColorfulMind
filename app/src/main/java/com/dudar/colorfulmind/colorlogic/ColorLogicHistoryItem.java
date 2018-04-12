package com.dudar.colorfulmind.colorlogic;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class ColorLogicHistoryItem implements Parcelable{
    private int color1, color2, color3, color4;
    private int blackBull, whiteCow;

    public ColorLogicHistoryItem(ColorLogicItem attemptColors, int colBull, int colCow) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        //parcel.writeInt(color1);
        //parcel.writeInt(color2);
        //parcel.writeInt(color3);
        //parcel.writeInt(color4);
        //parcel.writeInt(blackBull);
        //parcel.writeInt(whiteCow);
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
}

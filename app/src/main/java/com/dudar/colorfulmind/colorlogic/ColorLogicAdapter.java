package com.dudar.colorfulmind.colorlogic;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dudar.colorfulmind.R;

import java.util.ArrayList;

public class ColorLogicAdapter extends ArrayAdapter<ColorLogicHistoryItem>{
    public ColorLogicAdapter(Activity context, ArrayList<ColorLogicHistoryItem> items) {
        super(context, 0,items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.colors_list_item,
                                                parent,false);
        }

        ColorLogicHistoryItem currentHistoryItem = getItem(position);

        ImageView imgColor1 = (ImageView) listItemView.findViewById(R.id.imgColor1);
        ImageView imgColor2 = (ImageView) listItemView.findViewById(R.id.imgColor2);
        ImageView imgColor3 = (ImageView) listItemView.findViewById(R.id.imgColor3);
        ImageView imgColor4 = (ImageView) listItemView.findViewById(R.id.imgColor4);
        TextView bullsNumber = (TextView) listItemView.findViewById(R.id.tvBNumber);
        TextView cowsNumber = (TextView) listItemView.findViewById(R.id.tvCNumber);

        imgColor1.setBackgroundResource(currentHistoryItem.getColor1());
        imgColor2.setBackgroundResource(currentHistoryItem.getColor2());
        imgColor3.setBackgroundResource(currentHistoryItem.getColor3());
        imgColor4.setBackgroundResource(currentHistoryItem.getColor4());

        bullsNumber.setText(String.valueOf(currentHistoryItem.getBlackBullNumber()));
        cowsNumber.setText(String.valueOf(currentHistoryItem.getWhiteCowNumber()));

        return listItemView;
    }
}

package com.team4.social_geocaching_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Johnny on 11/5/2015.
 */
public class ListAdapter extends ArrayAdapter {
    private final Context context;
    private final ArrayList<RowItem> rowItemsArrayList;

    public ListAdapter(Context context, ArrayList<RowItem> rowItemsArrayList){
        super(context, R.layout.connectrow, rowItemsArrayList);
        this.context = context;
        this.rowItemsArrayList = rowItemsArrayList;
    }

    @Override
    public View getView(int position, View connectView, ViewGroup parent){
        /*LayoutInflater myInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = myInflater.inflate(R.layout.connectrow, null);*/
        View rowView = connectView;
        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.connectrow, null);
        }

        TextView activityView = (TextView) rowView.findViewById(R.id.connectrowitem);
        TextView dateView = (TextView) rowView.findViewById(R.id.connectrowtime);
        activityView.setText(rowItemsArrayList.get(position).activityText);
        dateView.setText(rowItemsArrayList.get(position).dateTime);
        return rowView;
    }
}

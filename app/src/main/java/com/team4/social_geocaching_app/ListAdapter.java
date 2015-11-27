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
 * Class which handles the dynamic display of row items using a list of data
 */
public class ListAdapter extends ArrayAdapter {
    //contains the context being used and a list of row items
    private final Context context;
    private final ArrayList<RowItem> rowItemsArrayList;

    //list adapter constructor
    public ListAdapter(Context context, ArrayList<RowItem> rowItemsArrayList){
        //initialize variables
        super(context, R.layout.connectrow, rowItemsArrayList);
        this.context = context;
        this.rowItemsArrayList = rowItemsArrayList;
    }

    @Override
    public View getView(int position, View connectView, ViewGroup parent){
        //set the rowview
        View rowView = connectView;
        // reuse views
        if (rowView == null) {
            //if the row view is not null then inflate for display
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.connectrow, null);
        }

        //get the text views and set the values to the values in the list
        TextView activityView = (TextView) rowView.findViewById(R.id.connectrowitem);
        TextView dateView = (TextView) rowView.findViewById(R.id.connectrowtime);
        activityView.setText(rowItemsArrayList.get(position).activityText);
        dateView.setText(rowItemsArrayList.get(position).dateTime);
        return rowView;
    }
}

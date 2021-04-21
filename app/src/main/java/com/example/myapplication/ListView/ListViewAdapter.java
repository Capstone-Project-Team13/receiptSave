package com.example.myapplication.ListView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ListViewAdapter extends BaseAdapter {
    private static final int ITEM_VIEW_TYPE_IMAGE = 1;
    private static final int ITEM_VIEW_TYPE_STRING = 2;
    private static final String TAG = "Food";
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>() ;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
    Calendar cal = Calendar.getInstance();
    Calendar cal2 = Calendar.getInstance();
    Date date = new Date();

    public ListViewAdapter(){

    }
    // Return adapter data size
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getItemViewType(int position) {
        return listViewItemList.get(position).getType();
    }

    // return data that will use print view
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ListViewItem listViewItem = listViewItemList.get(position);
            switch (getItemViewType(position)) {
                case ITEM_VIEW_TYPE_IMAGE:
                    convertView = inflater.inflate(R.layout.listview_item, parent, false);
                    ImageView iconImageView = convertView.findViewById(R.id.imageView);
                    TextView textTextView = convertView.findViewById(R.id.textView);
                    iconImageView.setImageDrawable(listViewItem.getIcon());
                    textTextView.setText(listViewItem.getText());
                    break;
                case ITEM_VIEW_TYPE_STRING:
                    Date exDate = null;
                    try {
                        // get expDate.
                        exDate = simpleDateFormat.parse(listViewItem.getExpDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    // set today + 3 and today + 10
                    cal.setTime(date);
                    cal2.setTime(date);
                    //cal.add(Calendar.DATE, 3);
                    cal2.add(Calendar.DATE, 7);
                    String today = simpleDateFormat.format(cal.getTime());
                    String today2 = simpleDateFormat.format(cal2.getTime());
                    Date toDays = null;
                    Date toDays2 = null;
                    try {
                        toDays = simpleDateFormat.parse(today);
                        toDays2 = simpleDateFormat.parse(today2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    convertView = inflater.inflate(R.layout.listview_fooditem, parent, false);
                    TextView itemTextView = convertView.findViewById(R.id.fooditems);
                    TextView dateTextView = convertView.findViewById(R.id.expDates);
                    // compare exdate with today
                        // color red
                    if(exDate.before(toDays) ){
                        dateTextView.setBackgroundColor(Color.RED);
                        // color yellow
                    }else if(exDate.after(toDays) && exDate.before(toDays2)){
                        dateTextView.setBackgroundColor(Color.YELLOW);
                        // color green
                    } else{
                        dateTextView.setBackgroundColor(Color.GREEN);
                    }
                    itemTextView.setText(listViewItem.getItemName());
                    dateTextView.setText(listViewItem.getExpDate());
                    break;
            }
        }
        return convertView;
    }

    public void addItem(Drawable icon, String text) {
        ListViewItem item = new ListViewItem();
        item.setType(ITEM_VIEW_TYPE_IMAGE);
        item.setIcon(icon);
        item.setText(text);
        listViewItemList.add(item);
    }
    public void addItem(String itemName, String expDate)  {
        ListViewItem item = new ListViewItem();
        item.setType(ITEM_VIEW_TYPE_STRING);
        item.setItemName(itemName);
        item.setExpDate(expDate);
        listViewItemList.add(item);
    }
}

package com.example.myapplication.ListView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private static final int ITEM_VIEW_TYPE_IMAGE = 1;
    private static final int ITEM_VIEW_TYPE_STRING = 2;
    private static final String TAG = "Food";
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>() ;

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
                    convertView = inflater.inflate(R.layout.listview_fooditem, parent, false);
                    TextView itemTextView = convertView.findViewById(R.id.fooditems);
                    TextView dateTextView = convertView.findViewById(R.id.expDates);
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
    public void addItem(String itemName, String expDate) {
        ListViewItem item = new ListViewItem();
        item.setType(ITEM_VIEW_TYPE_STRING);
        item.setItemName(itemName);
        item.setExpDate(expDate);
        listViewItemList.add(item);
    }
}

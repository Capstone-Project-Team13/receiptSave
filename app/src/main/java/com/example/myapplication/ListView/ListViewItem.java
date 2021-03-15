package com.example.myapplication.ListView;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class ListViewItem {
        private Drawable img;
        private String item;
        private String itemName;
        private String expDate;
        private int type;

        public void setIcon(Drawable icon){
            this.img = icon;
        }
        public void setText(String text) {
            this.item = text;
        }
        public void setItemName(String item_name) {
            this.itemName = item_name;
        }
        public void setExpDate(String exp_date) {
            this.expDate = exp_date;
        }
        public void setType(int type) {
            this.type = type;
        }

        public Drawable getIcon() {
            return this.img;
        }

        public String getText() {
            return this.item;
        }

        public String getItemName() {
            return this.itemName;
        }

        public String getExpDate() {
            return this.expDate;
        }

        public int getType() {
            return this.type;
        }
}

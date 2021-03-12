package com.example.myapplication;

import android.graphics.drawable.Drawable;

public class ListViewItem {
        private Drawable img;
        private String item;

        public void setIcon(Drawable icon){
            this.img = icon;
        }
        public void setText(String text) {
            this.item = text ;
        }

        public Drawable getIcon() {
            return this.img ;
        }

        public String getText() {
            return this.item ;
        }
}

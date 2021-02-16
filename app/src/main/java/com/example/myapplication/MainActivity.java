package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnAction;
    private CardView scanCard,receiptCard,foodCard,recipeCard,settingCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //defining cards
        scanCard = (CardView)  findViewById(R.id.scan_receipt_card);
        receiptCard = (CardView)  findViewById(R.id.receipt_history_card);
        foodCard  = (CardView)  findViewById(R.id.my_food_card);
        recipeCard  = (CardView)  findViewById(R.id.get_recipe_card);
        settingCard  = (CardView)  findViewById(R.id.setting_card);
        //add click listener to the cards
        scanCard.setOnClickListener(this);
        receiptCard.setOnClickListener(this);
        foodCard.setOnClickListener(this);
        recipeCard.setOnClickListener(this);
        settingCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()) {
            case R.id.scan_receipt_card: i = new Intent(this,Scan.class);startActivity(i);break;
            case R.id.receipt_history_card: i = new Intent(this,Receipt.class);startActivity(i);break;
            case R.id.my_food_card: i = new Intent(this,Food.class);startActivity(i);break;
            case R.id.get_recipe_card: i = new Intent(this,Recipe.class);startActivity(i);break;
            case R.id.setting_card: i = new Intent(this,Setting.class);startActivity(i);break;

            default:break ;
        }
    }
}
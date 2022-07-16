package com.example.labo3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ProductsByCategory extends AppCompatActivity {
TextView tvCateg;
ListView listViewCateg;

ArrayList<Produit> listeProdCategorie = new ArrayList<>();
ArrayAdapter<Produit> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_by_category);

        //button pour returner à MainActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvCateg = findViewById(R.id.tvCateg);
        listViewCateg = findViewById(R.id.listViewCateg);

        Intent i = getIntent();
    //    listeProdCategorie = i.getParcelableExtra("listeProduitsCateg");
        listeProdCategorie =this.getIntent().getExtras().getParcelableArrayList("listeProduitsCateg");
        Log.d("listeProdCategory", listeProdCategorie.toString());

        tvCateg.setText(listeProdCategorie.get(0).getCateg());


        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listeProdCategorie);
        listViewCateg.setAdapter(adapter);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
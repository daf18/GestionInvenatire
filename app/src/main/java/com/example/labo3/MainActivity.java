package com.example.labo3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    ArrayList<Produit> listeProduits = new ArrayList<>(); //TODO check if it should be init here
    ArrayAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        listView = findViewById(R.id.listView);
//        adapter = new ArrayAdapter<Produit>(this,
//                android.R.layout.simple_list_item_1, listeProduits);
//
          chargerProduits();
    }
    public void chargerProduits(){
        String string = "";
        StringBuilder stringBuilder = new StringBuilder();
        InputStream is = null;
        try {
            is = getAssets().open("produits");
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        while (true) {
            try {
                if ((string = reader.readLine()) == null) break;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            String[] parts = string.split(";", 5);

            int id = Integer.parseInt(parts[0]);
            String nom = parts[1];
            String categ = parts[2];
            double prix = Double.parseDouble(parts[3]);
            int qte = Integer.parseInt(parts[4]);
            listeProduits.add(new Produit(id,nom,categ,prix,qte));
            stringBuilder.append(listeProduits.toString());

         //   textView.setText(stringBuilder); //TODO remove
                    adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listeProduits);
            listView.setAdapter(adapter);

//            for (String a : parts) {
//                 System.out.println(a);
//                stringBuilder.append(a).append("\n");
//            }
//              stringBuilder.append(string).append("\n");
//
//            textView.setText(stringBuilder);
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getBaseContext(), stringBuilder.toString(),
                Toast.LENGTH_LONG).show();
    }
}



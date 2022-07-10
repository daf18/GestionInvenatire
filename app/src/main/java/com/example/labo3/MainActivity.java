package com.example.labo3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Produit> listeProduits = new ArrayList<>(); //TODO check if it should be init here
    ArrayAdapter adapter;
    ListView listView;
Button btnLister, btnAjouter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        btnLister = findViewById(R.id.btnLister);
        btnAjouter = findViewById(R.id.btnAjouter);

          chargerProduits();

          btnLister.setOnClickListener(view -> listerProduits());

          btnAjouter.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  openAddProduct();
              }
          });
        ajoutNouveauProduit();
    }
    public void chargerProduits(){
        String string = "";
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
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void listerProduits(){
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listeProduits);
        listView.setAdapter(adapter);
    }

    private void openAddProduct(){
        Intent intent = new Intent(getApplicationContext(),AddProduct.class);
        startActivity(intent);
    }
    public void ajoutNouveauProduit(){
        Intent i = getIntent();
        Produit nProduit = (Produit) i.getParcelableExtra("produit");
        if(nProduit != null)
        Log.i("produitMain",nProduit.toString());
        listeProduits.add(nProduit);

    }
}



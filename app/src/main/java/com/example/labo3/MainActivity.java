package com.example.labo3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //TODO check if it should be init here
    ArrayList<Produit> listeProduits = new ArrayList<>();
    ArrayAdapter adapter;
    ListView listView;
    Button btnLister, btnAjouter,btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        btnLister = findViewById(R.id.btnLister);
        btnAjouter = findViewById(R.id.btnAjouter);
        btnSave = findViewById(R.id.btnSave);

        chargerProduits();

          btnLister.setOnClickListener(view -> listerProduits());

          btnAjouter.setOnClickListener(view -> openAddProduct());

         btnSave.setOnClickListener(view -> saveProducts());

//        if(!isExternalStorageAvailableForRw()){
//            btnSave.setEnabled(false);
//        }

  //        ajoutNouveauProduit();
    }



    public void chargerProduits(){
        String string = "";
        FileInputStream fis = null;
        try {
            fis = openFileInput("produits.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
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
            assert fis != null;
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveProducts() {
        ajoutNouveauProduit();

        StringBuilder filecontent = new StringBuilder();

        for(Produit unProd : listeProduits) {
            filecontent.append(insert(unProd));
        }
        Log.i("filecontent", filecontent.toString());

        FileOutputStream fos = null;
        try {
            fos = openFileOutput("produits.txt", MODE_PRIVATE);
            fos.write(filecontent.toString().getBytes());

            Toast.makeText(this, "Saved to "+getFilesDir(),Toast.LENGTH_LONG).show();

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(fos !=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
        Produit nProduit = i.getParcelableExtra("produit");
        if(nProduit != null)
        Log.i("produitMain",nProduit.toString());
        listeProduits.add(nProduit);
        Log.e("list",listeProduits.toString());
    }

    //methode privée pour transformer le produit en string en ayant comme delimiteur ";"
    private String insert(Produit unProduit) {
        String prodString;

        prodString = unProduit.getId()+";"
                +unProduit.getNom()+";"
                +unProduit.getCateg()+";"
                +unProduit.getPrix()+";"
                +unProduit.getQte()+"\n";

        return prodString;
    }
}



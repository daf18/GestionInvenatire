package com.example.labo3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Produit> listeProduits = new ArrayList<>(); //TODO check if it should be init here
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
//        FileInputStream fis = null;
//
//        try {
//            fis = openFileInput("produits.txt");
//            InputStreamReader isr = new InputStreamReader(fis);
//            BufferedReader br = new BufferedReader(isr);
//            StringBuilder sb = new StringBuilder();
//            String text;
//
//            while((text = br.readLine()) != null){
//                sb.append(text).append("\n");
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveProducts() {
        ajoutNouveauProduit();
//        try {
//            BufferedWriter bw = new BufferedWriter(new FileWriter("assets/produits.txt"));
//            for (Produit unProd : listeProduits) {
//                bw.write(insert(unProd));
//                bw.newLine();
//            }
//            bw.close();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        String filecontent ="";

        for(Produit unProd : listeProduits) {
            filecontent += insert(unProd);
        }
        Log.i("filecontent",filecontent);

        FileOutputStream fos = null;
        try {
            fos = openFileOutput("produits.txt", MODE_PRIVATE);
            fos.write(filecontent.getBytes());

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
        Produit nProduit = (Produit) i.getParcelableExtra("produit");
        if(nProduit != null)
        Log.i("produitMain",nProduit.toString());
        listeProduits.add(nProduit);
        Log.e("list",listeProduits.toString());
    }

//        //TODO daca nu am prod nou si dau click it crashes
//        for(Produit unProd : listeProduits){
//            filecontent += insert(unProd);
//        }
//        if(!filecontent.equals("")){
//            File myExternalFile = new File(getExternalFilesDir(filepath), filename);
//            FileOutputStream fos = null;
//            try {
//                fos = new FileOutputStream(myExternalFile);
//                fos.write(filecontent.getBytes());
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Toast.makeText(this, "Info Saved to file",Toast.LENGTH_SHORT).show();
//        }

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



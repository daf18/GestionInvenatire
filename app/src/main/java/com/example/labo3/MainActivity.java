package com.example.labo3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;

    //TODO check if it should be init here
    ArrayList<Produit> listeProduits = new ArrayList<>();
    ArrayAdapter adapter;
    ListView listView;
    Button btnLister, btnAjouter,btnSave;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout= findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        //ajouter ToogleBar
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.menu_open,R.string.menu_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        // nous donne la fleche de retour a l'activité parent
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ajouter les listeners pour les activités
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_lister:
                        Log.i("drawerMenu","Lister was clicked"+ listeProduits);
                        listerProduits();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_ajouter:
                        Log.i("drawerMenu","Ajouter was clicked");
                      //  openAddProduct();
                        sauvegarderProduits();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_categ:
                        Log.i("drawerMenu","Categorie was clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_total:
                        Log.i("drawerMenu","Total was clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }

                return true;
            }
        });


        listView = findViewById(R.id.listView);
        btnLister = findViewById(R.id.btnLister);
        btnAjouter = findViewById(R.id.btnAjouter);
        btnSave = findViewById(R.id.btnSave);

        chargerProduits();

 //         btnLister.setOnClickListener(view -> listerProduits());

   //       btnAjouter.setOnClickListener(view -> openAddProduct()); // sauvegarderProduits()

//         btnSave.setOnClickListener(view -> sauvegarderProduits());

//        if(!isExternalStorageAvailableForRw()){
//            btnSave.setEnabled(false);
//        }

  //        ajoutNouveauProduit();
    }


//TODO handle if file doesn't exist?
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
//liste les produits dans un ListView
    public void listerProduits(){
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listeProduits);
        listView.setAdapter(adapter);
    }

//    private void openAddProduct(){
//
//        sauvegarderProduits();
//    }
  //sauvegarde les produis de la listeProduits dans le fichier profuits.txt  TODO recupereaza produsul din addProd activity
    public void sauvegarderProduits() {
        //ouvre l'activité add_product pout le formulaire ajouter produit
        Intent intent = new Intent(getApplicationContext(),AddProduct.class);
        startActivity(intent);

 //       ajoutNouveauProduit();

        //recupère le produit crée dans l'activité add_product et l'ajoute au listeProduits
        Intent i = getIntent();
        Produit nProduit = i.getParcelableExtra("produit");
        if(nProduit != null)
            Log.i("produitMain",nProduit.toString());
        listeProduits.add(nProduit);
        Log.e("list",listeProduits.toString());

//ecrire le contenu du listeProduits dans le fichier produits.txt
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

//    private void ajoutNouveauProduit(){
//
//    }

    //methode privée pour transformer le produit en string en ayant comme delimiteur ";"
    private String insert(Produit unProduit) {
        String prodString = "";
 if (unProduit != null) {
     prodString = unProduit.getId() + ";"
             + unProduit.getNom() + ";"
             + unProduit.getCateg() + ";"
             + unProduit.getPrix() + ";"
             + unProduit.getQte() + "\n";
 }
        return prodString;
    }
}



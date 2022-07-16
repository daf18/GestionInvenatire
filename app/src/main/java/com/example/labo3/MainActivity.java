package com.example.labo3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Spinner spinnerCateg;
    Button btnSave;
    Produit nProduit;

    ArrayList<Produit> listeProduits = new ArrayList<>();
    ArrayList<Produit> listeProduitsCateg = new ArrayList<>();
    ArrayList<String> listeCateg = new ArrayList<>();
    String categorie;

    ArrayAdapter<Produit> adapter;
    ArrayAdapter<String> adapterCategorie; //
    ListView listView;

    TextView tvTotalMain;

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

        listView = findViewById(R.id.listView);
        btnSave = findViewById(R.id.btnSave);

        tvTotalMain = findViewById(R.id.tvTotalMain);

        spinnerCateg = findViewById(R.id.spinnerCateg);

        drawerLayout= findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        //ajouter ToogleBar
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.menu_open,R.string.menu_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        // nous donne la fleche de retour a l'activité parent
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //ajouter les listeners pour les activités
        demarerActivite();

        //  creerListeCateg();
        //   TODO disable spinner
       // spinnerCateg.setEnabled(false);

        //charger le produits du produits.txt
        chargerProduits();

        //set up spinner TODO comment
        adapterCategorie = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, listeCateg);
        adapterCategorie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCateg.setAdapter(adapterCategorie);
        adapterCategorie.notifyDataSetChanged();


        spinnerCateg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                //parent.getItemAtPosition(pos).toString()
              //  Toast.makeText(parent.getContext(), listeCateg.get(pos), Toast.LENGTH_SHORT).show();
//                Log.d("selected", "CategSelected ");
                categorie = listeCateg.get(pos);


                Log.d("CategorieSpinner",categorie);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d("selected", "onNothingSelected: ");
            }
        });

        btnSave.setOnClickListener(view -> {
            sauvegarderProduits();
            Toast.makeText(MainActivity.this,"Sauvgarde terminée avec succès",Toast.LENGTH_LONG).show();
        });

        //   listerProduits();
    }

    public void demarerActivite(){
        navigationView.setNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.nav_lister:
                    resetView();
                    listerProduits();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;

                case R.id.nav_ajouter:
                    //sauvegarderProduits();
                    ouvrirAjouterProduitActivity();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;

                case R.id.nav_categ:
                    if(categorie.equalsIgnoreCase("Choisir la catégorie")){
                        Toast.makeText(MainActivity.this,"Choissisez une catégorie",Toast.LENGTH_LONG).show();
                    }else {
                        listeProduitSelonCategorie(categorie);
                    }

                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;

                case R.id.nav_total:
                    totalInventaire();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
            }
            return true;
        });
    }

    public void chargerProduits(){

        //verifie si le fichier produits.txt existe sinon il sera crée
        String FILE = getFilesDir()+"/produits.txt";
        Log.e("FILE",FILE);
        //verifie si l'appareil a un Android SDK >= 26
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Path textFilePath = Paths.get(FILE);
            try {
                Files.createFile(textFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//lis fichier produits.txt
        String string = "";
        FileInputStream fis = null;
listeCateg.add(0,"Choisir la catégorie");
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
            String nom = parts[1].trim();
            String categ = parts[2].trim();
            double prix = Double.parseDouble(parts[3]);
            int qte = Integer.parseInt(parts[4]);
//ajoute un nouveau produit à la liste de produits
            listeProduits.add(new Produit(id,nom,categ,prix,qte));

            //ajout categ à listeCateg
            if ( !listeCateg.contains(categ)){
                listeCateg.add(categ.trim());
            }
        }
        try {
            assert fis != null;
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //sauvegarde les produis de la listeProduits dans le fichier profuits.txt
    public void sauvegarderProduits() {
        //       ajoutNouveauProduit();
        StringBuilder filecontent = new StringBuilder();

        for(Produit unProd : listeProduits) {
            filecontent.append(insert(unProd));
        }
        Log.i("filecontent", filecontent.toString());

        FileOutputStream fos = null;
        try {
            fos = openFileOutput("produits.txt", MODE_PRIVATE);
            fos.write(filecontent.toString().getBytes());

            //          Toast.makeText(this, "Saved to "+getFilesDir(),Toast.LENGTH_LONG).show();

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

    //affiche listeProduit dans une listeView
    public void listerProduits(){
        if(!listeProduits.contains(nProduit)){
            ajoutNouveauProduit();
        }

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listeProduits);
        listView.setAdapter(adapter);
    }
    //ouvre l'activité add_product pour le formulaire ajouter produit
    private void ouvrirAjouterProduitActivity(){
        Intent intent = new Intent(getApplicationContext(),AddProduct.class);
        startActivity(intent);
    }
    //recupère le produit crée dans l'activité add_product et l'ajoute au listeProduits
    public void ajoutNouveauProduit(){
        Intent i = getIntent();
        //      Produit nProduit = i.getParcelableExtra("produit");
        nProduit = i.getParcelableExtra("produit");
        if(nProduit != null) {
            Log.i("produitMain", nProduit.toString());
            listeProduits.add(nProduit);
            btnSave.setVisibility(View.VISIBLE); //TODO added here
            //ajout categ à listeCateg
            if ( !listeCateg.contains(nProduit.getCateg())){
                listeCateg.add(nProduit.getCateg().trim());
            }
        }
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

    public void listeProduitSelonCategorie(String categ){
     //   spinnerCateg.setVisibility(View.VISIBLE);
        //ajouter la catégorie à la liste de catégories( si pas déjà presante )
        for(Produit prod : listeProduits){
            if(prod.getCateg().equals(categ)){
                listeProduitsCateg.add(prod);
                Log.d("ProdCateg", listeProduitsCateg.toString());
            }
        }
        Intent intentCateg = new Intent(getApplicationContext(),ProductsByCategory.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("listeProduitsCateg", listeProduitsCateg);
        intentCateg.putExtras(bundle);
        startActivity(intentCateg);
        //finish();
    }
    public void totalInventaire(){
        Double total = 0.00;
        for(Produit prod: listeProduits){
            if(prod.getQte() > 0){
                total += prod.getPrix()*prod.getQte();
            }
        }
        Log.d("total",total.toString());
        //cacher spinner
        spinnerCateg.setVisibility(View.GONE);
        //cacher listView
        listView.setVisibility(View.GONE);

        tvTotalMain.setVisibility(View.VISIBLE);
        tvTotalMain.setText("Le montant total de l'inventaire est : " + total.toString() + " $");

    }
    public void resetView(){
        tvTotalMain.setVisibility(View.GONE);
        //cacher spinner
        spinnerCateg.setVisibility(View.VISIBLE);
        //cacher listView
        listView.setVisibility(View.VISIBLE);

    }

}



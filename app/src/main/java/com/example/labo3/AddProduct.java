package com.example.labo3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AddProduct extends AppCompatActivity {
EditText etId, etName, etCateg, etPrice, etQuantity;
Button btnCancel, btnAdd;
boolean isAllFieldsChecked;
Produit produit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);

        etId = findViewById(R.id.etId);
        etName = findViewById(R.id.etName);
        etCateg = findViewById(R.id.etCateg);
        etPrice = findViewById(R.id.etPrice);
        etQuantity = findViewById(R.id.etQuantity);

        btnCancel = findViewById(R.id.btnCancel);
        btnAdd = findViewById(R.id.btnAdd);

        isAllFieldsChecked = false;

btnCancel.setOnClickListener(view -> finish());

btnAdd.setOnClickListener(view -> addProduct());

    }
    public void addProduct(){

        isAllFieldsChecked = checkAllFields();
        if (isAllFieldsChecked) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            //envoyer le produit a MainActivity
            intent.putExtra("produit", (Parcelable) produit);
            startActivity(intent);
            finish();
        }
    }
    private boolean checkAllFields(){
        String id = etId.getText().toString();
        String name = etName.getText().toString();
        String categ = etCateg.getText().toString();
        String price = etPrice.getText().toString();
        String qte = etQuantity.getText().toString();

        if (etId.length() == 0) {
            etId.setError("Identifient du produit requis!");
            return false;
        } else if(id.matches("/^\\d+$/")) {
            etId.setError("Id contient des caractères invalides");
            return false;
        }

        if (etName.length() == 0) {
            etName.setError("Nom du produit requis!");
            return false;
        }else if(name.matches("/^\\w+/")) {
            etName.setError("Nom contient des caractères invalides");
            return false;
        }

        if (etCateg.length() == 0) {
            etCateg.setError("Catégorie du produit requis!");
            return false;
        }else if(categ.matches("/^\\w+/")) {
            etCateg.setError("Catégorie contient des caractères invalides");
            return false;
        }
        if (etPrice.length() == 0) {
            etPrice.setError("Prix du produit requis!");
            return false;
        }else if(price.matches("/^[0-9]+.[0-9]+$")) {
            etPrice.setError("Prix contient des caractères invalides");
            return false;
        }

        if (etQuantity.length() == 0) {
            etQuantity.setError("Quantité du produit requis!");
            return false;
        }else if(qte.matches("/^\\d+$/")) {
            etQuantity.setError("Quantité contient des caractères invalides");
            return false;
        }
        Toast.makeText(this,"Produit "+id+" a été bien enregistré", Toast.LENGTH_LONG).show();
        produit = new Produit(Integer.parseInt(id),
                name,
                categ,
                Double.parseDouble(price),
                Integer.parseInt(qte));
        Log.i("produit",produit.toString());
    // si tous les champs sont remplis , retourner true
        return true;
    }

}
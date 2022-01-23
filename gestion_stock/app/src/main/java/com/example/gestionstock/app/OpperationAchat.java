package com.example.gestionstock.app;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.couchbase.lite.Blob;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Database;
import com.couchbase.lite.DictionaryInterface;
import com.couchbase.lite.Expression;
import com.couchbase.lite.Meta;
import com.couchbase.lite.MutableDocument;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.Result;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;
import com.example.gestionstock.R;
import com.example.gestionstock.beans.ProduitRowItem;
import com.example.gestionstock.login.LoginActivity;
import com.example.gestionstock.util.DatabaseManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OpperationAchat extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Date d = new Date();
    Database database = DatabaseManager.getDatabase();

    EditText quantiteAcheteInput;
    Spinner shooseFourns;
    String fournisseurSelected;
    String qProd, idProd;
    String[] furnisseurs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achat);

        qProd = getIntent().getStringExtra("produit_quantite");
        idProd = getIntent().getStringExtra("produit_id");

        quantiteAcheteInput = findViewById(R.id.quantiteAcheteInput);
        shooseFourns = findViewById(R.id.fournsseurspinner);
        shooseFourns.setOnItemSelectedListener(this);

        Query query = QueryBuilder.select(SelectResult.all(), SelectResult.expression(Meta.id))
                .from(DataSource.database(database)).where(
                        Expression.property("type").equalTo(Expression.string("fournisseur")));
        ResultSet resultSet = null;
        ResultSet resultSet1 = null;

        try {
            int i = 0;
            int size = 0;
            resultSet = query.execute();
            resultSet1 = query.execute();
            for (Result result : resultSet1) {
                assert result != null;
                size++;
            }
            furnisseurs = new String[size];

            for (Result result : resultSet) {
                assert result != null;
                DictionaryInterface e = result.getDictionary("db");
                String f = e.getString("name");
                furnisseurs[i]=f;
                i++;
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

        ArrayAdapter shooseFournsAd = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                furnisseurs);

        shooseFournsAd.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        shooseFourns.setAdapter(shooseFournsAd);

        getSupportActionBar().setTitle("Achat du produit : "+ getIntent().getStringExtra("produit_libelle"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void onReturnTapped(View view) {
        gotToHome();
    }

    public void gotToHome() {
        Intent intent = new Intent(getApplicationContext(), Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public void onSaveTapped(View view) {
        if(quantiteAcheteInput.length() > 0){
            //if(Integer.parseInt(quantiteAcheteInput.getText().toString()) > Integer.parseInt(qProd)){

            MutableDocument prod = database.getDocument(idProd).toMutable();
            int newQuantite = Integer.parseInt(quantiteAcheteInput.getText().toString()) + Integer.parseInt(qProd);
            prod.setString("quantite", Integer.toString(newQuantite));


            String docId = "achat_"+d.hashCode();
            String idFournisseur = "";

            Query query = QueryBuilder.select(SelectResult.all(), SelectResult.expression(Meta.id))
                    .from(DataSource.database(database)).where(
                            Expression.property("type").equalTo(Expression.string("fournisseur"))
                                    .and(Expression.property("name").equalTo(Expression.string(fournisseurSelected))));
            ResultSet resultSet = null;

            try {
                resultSet = query.execute();
                for (Result result : resultSet) {
                    assert result != null;
                    idFournisseur = result.getString("id");
                }
            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }

            Map<String, Object> map = new HashMap<>();
            map.put("quantite", quantiteAcheteInput.getText().toString());
            map.put("type", "achat");
            map.put("id_fournisseur", idFournisseur);
            map.put("id_produit", idProd);

            MutableDocument mutableDocument = new MutableDocument(docId, map);
            try {
                database.save(mutableDocument);
                database.save(prod);

            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "Achat realisé avec succée!", Toast.LENGTH_SHORT).show();
            gotToHome();


        }else{
        Toast.makeText(this, "Tous les champs  sont obligatoirs !", Toast.LENGTH_SHORT).show();
    }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onLogOut(MenuItem mi) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        fournisseurSelected = furnisseurs[position];

        Toast.makeText(getApplicationContext(),
                fournisseurSelected,
                Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


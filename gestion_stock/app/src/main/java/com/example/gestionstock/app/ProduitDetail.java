package com.example.gestionstock.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.couchbase.lite.Blob;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Meta;
import com.couchbase.lite.MutableDocument;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.Result;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;
import com.example.gestionstock.R;
import com.example.gestionstock.login.LoginActivity;
import com.example.gestionstock.util.DatabaseManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProduitDetail extends AppCompatActivity {
    private Date d = new Date();
    Database database = DatabaseManager.getDatabase();

    EditText libelleInput;
    EditText quantiteInput;
    EditText prixInput;
    ImageView imageView;
    String prodQuantite, prodId, prodLibelle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produit_detail);

        libelleInput = findViewById(R.id.produitSelectedLibelle);
        quantiteInput = findViewById(R.id.produitSelectedQuantite);
        prixInput = findViewById(R.id.produitSelectedPrix);
        imageView = findViewById(R.id.produitSelectedImage);

        getSupportActionBar().setTitle("Produit " + getIntent().getStringExtra("produit_libelle"));

        prodId = getIntent().getStringExtra("produit_id");


        Document prod =  database.getDocument(prodId);

        prodLibelle = prod.getString("libelle");
        prodQuantite = prod.getString("quantite");

        libelleInput.setText(prod.getString("libelle"));
        quantiteInput.setText(prod.getString("quantite"));
        prixInput.setText(prod.getString("prix"));
        imageView.setImageDrawable(Drawable.createFromStream(prod.getBlob("image").getContentStream(), "res"));
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
    public void acheterTapped(View view) {
        Intent intent = new Intent(getApplicationContext(), OpperationAchat.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("produit_id", prodId);
        intent.putExtra("produit_libelle", prodLibelle);
        intent.putExtra("produit_quantite", prodQuantite);
        startActivity(intent);
    }

    public void vendreTapped(View view) {
        Intent intent = new Intent(getApplicationContext(), OpperationVente.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("produit_id", prodId);
        intent.putExtra("produit_libelle", prodLibelle);
        intent.putExtra("produit_quantite", prodQuantite);
        startActivity(intent);
    }

    public void onEditTapped(View view) {
        Intent intent = new Intent(getApplicationContext(), EditProduit.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("produit_id", prodId);
        intent.putExtra("produit_libelle", prodLibelle);
        startActivity(intent);
    }

    public void onDeleteTapped(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Confirmer");
        alert.setMessage("Confirmer la suppression du produit");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                MutableDocument prod = database.getDocument(prodId).toMutable();

                try {
                    database.delete(prod);
                } catch (CouchbaseLiteException e) {
                    e.printStackTrace();
                }
                gotToHome();
                dialog.dismiss();
            }
        });
        alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
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

    public void returnTapped(View view) {
        Intent intent = new Intent(getApplicationContext(), Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}

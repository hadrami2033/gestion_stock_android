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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.couchbase.lite.Blob;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Database;
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

public class Produit extends AppCompatActivity {
    private Date d = new Date();
    Database database = DatabaseManager.getDatabase();

    EditText libelleInput;
    EditText quantiteInput;
    EditText prixInput;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produit);

        getSupportActionBar().setTitle("Ajouter un Produit"); // set the top title

        libelleInput = findViewById(R.id.libelleInput);
        quantiteInput = findViewById(R.id.quantiteInput);
        prixInput = findViewById(R.id.prixInput);
        imageView = findViewById(R.id.imageView);

    }

    public static final int PICK_IMAGE = 1;

    public void onUploadPhotoTapped(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri selectedImage = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException ex) {
                    Log.i("SelectPhoto", ex.getMessage());
                }
            }
        }
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
        if(libelleInput.length() > 0 && quantiteInput.length() > 0 && prixInput.length() > 0){
        String docId = "produit_"+d.hashCode();

        Map<String, Object> produit = new HashMap<>();
        produit.put("libelle", libelleInput.getText().toString());
        produit.put("quantite", quantiteInput.getText().toString());
        produit.put("prix", prixInput.getText().toString());
        produit.put("type", "produit");

        byte[] imageViewBytes = getImageViewBytes();

        if (imageViewBytes != null) {
            produit.put("image", new com.couchbase.lite.Blob("image/jpeg", imageViewBytes));
        }

        MutableDocument mutableDocument = new MutableDocument(docId, produit);


        try {
            database.save(mutableDocument);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "Produit a ajouté avec succée!", Toast.LENGTH_SHORT).show();
        gotToHome();
    }else{
        Toast.makeText(this, "Tous les champs  sont obligatoirs !", Toast.LENGTH_SHORT).show();
    }
    }

    private byte[] getImageViewBytes() {
        byte[] imageBytes = null;

        BitmapDrawable bmDrawable = (BitmapDrawable) imageView.getDrawable();

        if (bmDrawable != null) {
            Bitmap bitmap = bmDrawable.getBitmap();

            if (bitmap != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                imageBytes = baos.toByteArray();
            }
        }

        return imageBytes;
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

}

package com.example.gestionstock.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.couchbase.lite.Blob;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Database;
import com.couchbase.lite.DictionaryInterface;
import com.couchbase.lite.Expression;
import com.couchbase.lite.Meta;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.Result;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;
import com.example.gestionstock.R;
import com.example.gestionstock.beans.ProduitRowItem;
import com.example.gestionstock.login.LoginActivity;
import com.example.gestionstock.util.DatabaseManager;


import java.util.ArrayList;
import java.util.List;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.gestionstock.adapters.ProduitCustomListViewAdapter;

public class Home extends AppCompatActivity implements OnItemClickListener{

    Database database = DatabaseManager.getDatabase();

    ListView listView;
    List<ProduitRowItem> rowItems;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //getSupportActionBar().setTitle("Ajout de Client"); // set the top title

        /*getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);*/

        rowItems = new ArrayList<ProduitRowItem>();

        Query query = QueryBuilder.select(SelectResult.all(), SelectResult.expression(Meta.id))
                .from(DataSource.database(database)).where(
                Expression.property("type").equalTo(Expression.string("produit")));
        ResultSet resultSet = null;

        try {
            resultSet = query.execute();
            for (Result result : resultSet) {
                assert result != null;
                //Object e = result.toMap().get("db");
                DictionaryInterface e = result.getDictionary("db");

                Blob imageBlob = e.getBlob("image");

                String q = "Quantité : " + e.getString("quantite");
                String l = e.getString("libelle");
                Drawable d = Drawable.createFromStream(imageBlob.getContentStream(), "res");

                ProduitRowItem item = new ProduitRowItem(d, l, q);

                rowItems.add(item);
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }



       /* for (int i = 0; i < titles.length; i++) {
            RowItem item = new RowItem(images[i], titles[i], descriptions[i]);
            rowItems.add(item);
        }*/

        listView = (ListView) findViewById(R.id.listProduit);
        ProduitCustomListViewAdapter adapter = new ProduitCustomListViewAdapter(this,
                R.layout.produit_list_item, rowItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String idProd = "";

        Query query = QueryBuilder.select(SelectResult.all(), SelectResult.expression(Meta.id))
                .from(DataSource.database(database)).where(
                        Expression.property("type").equalTo(Expression.string("produit"))
                                .and(Expression.property("libelle").equalTo(Expression.string(rowItems.get(position).getLibelle())))
                                //.and(Expression.property("quantite").equalTo(Expression.string(rowItems.get(position).getQuantite())))
                        );
        ResultSet resultSet = null;

        try {
            resultSet = query.execute();
            for (Result result : resultSet) {
                assert result != null;
                idProd = result.getString("id");
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }


        Intent intent = new Intent(getApplicationContext(), ProduitDetail.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("produit_id", idProd);
        intent.putExtra("produit_libelle", rowItems.get(position).getLibelle());
        startActivity(intent);
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

    public void addProduitTapped(View view) {
        Intent intent = new Intent(getApplicationContext(), Produit.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void addClientTapped(View view) {
        Intent intent = new Intent(getApplicationContext(), Client.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void addFournisseurTapped(View view) {
        Intent intent = new Intent(getApplicationContext(), Fournisseur.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}

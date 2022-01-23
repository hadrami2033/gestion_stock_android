package com.example.gestionstock.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestionstock.R;
//import com.example.gestionstock.profile.UserProfileActivity;
import com.example.gestionstock.app.Home;
import com.example.gestionstock.app.Produit;
import com.example.gestionstock.util.DatabaseManager;

public class LoginActivity extends AppCompatActivity {

    EditText usernameInput;
    EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
    }

    public void onLoginTapped(View view) {
        System.out.println(usernameInput.getText().toString());
        System.out.println(passwordInput.getText().toString());

        if (usernameInput.getText().toString().equalsIgnoreCase("admin") && passwordInput.getText().toString().equalsIgnoreCase("admin")) {
            DatabaseManager dbMgr = DatabaseManager.getSharedInstance();
            dbMgr.initCouchbaseLite(getApplicationContext());
            dbMgr.openOrCreateDatabase(getApplicationContext());

            Intent intent = new Intent(getApplicationContext(), Home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Invalid Authentication !", Toast.LENGTH_SHORT).show();
        }
    }
}

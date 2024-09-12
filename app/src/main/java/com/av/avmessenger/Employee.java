package com.av.avmessenger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Employee extends AppCompatActivity {
    private ImageView autoris,avance;
    String ip="http://192.168.1.114:8080";
    int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee);
        autoris= findViewById(R.id.autori);
        avance= findViewById(R.id.avance);
        int userId = getIntent().getIntExtra("idUser", -1);

        autoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Employee.this, authorization.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

        avance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Employee.this, Avance.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

    }
}
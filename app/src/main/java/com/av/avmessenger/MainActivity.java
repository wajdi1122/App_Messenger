package com.av.avmessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.av.avmessenger.Class.Config;
import com.av.avmessenger.Class.UserAdapter;

import com.av.avmessenger.Class.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{


    RecyclerView mainUserRecyclerView;
    UserAdapter  adapter;
    ArrayList<User> usersArrayList;
    ImageView imglogout,touser,toavance,toauto,toadduser;
    ImageView cumbut,setbut;
    String ip= Config.BASE_URL;;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        int userId = getIntent().getIntExtra("idUser", -1);

        cumbut = findViewById(R.id.camBut);
        setbut = findViewById(R.id.settingBut);
        touser= (ImageView)findViewById(R.id.touser);
        toavance=findViewById(R.id.toavance);
        toauto=findViewById(R.id.toAuto);
        toadduser=findViewById(R.id.toadduser);
        usersArrayList = new ArrayList<>();
        toadduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, add_employee.class);
                intent.putExtra("id_user",userId);
                startActivity(intent);
            }
        });
        toavance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, List_avance.class);
                intent.putExtra("id_user",userId);
                startActivity(intent);
            }
        });
        toauto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, List_autorisation.class);
                intent.putExtra("id_user",userId);
                startActivity(intent);
            }
        });

        touser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, listUser.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });


        imglogout = findViewById(R.id.logoutimg);

        imglogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this,R.style.dialoge);
                dialog.setContentView(R.layout.dialog_layout);
                Button no,yes;
                yes = dialog.findViewById(R.id.yesbnt);
                no = dialog.findViewById(R.id.nobnt);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(MainActivity.this,login.class);
                        startActivity(intent);
                        finish();
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        setbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, setting.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

        cumbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, listGrp.class);
                intent.putExtra("id_user",userId);
                startActivity(intent);
            }
        });



    }
}
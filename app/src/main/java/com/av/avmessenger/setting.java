package com.av.avmessenger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.av.avmessenger.Class.Config;

import org.json.JSONException;
import org.json.JSONObject;

public class setting extends AppCompatActivity {
    ImageView setprofile;
    EditText setname, setprenom, setemail;
    Button donebut;
    ProgressDialog progressDialog;
    String ip = Config.BASE_URL;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().hide();

        setprofile = findViewById(R.id.settingprofile);
        setname = findViewById(R.id.settingname);
        setemail = findViewById(R.id.textViewEmail);
        setprenom = findViewById(R.id.settingstatus);
        donebut = findViewById(R.id.donebutt);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Modification en cours...");
        progressDialog.setCancelable(false);

        // Récupérer l'ID de l'utilisateur à partir de l'intent
        int userId = getIntent().getIntExtra("userId", -1);

        donebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Vérifiez si l'ID est valide
                if (userId != -1) {
                    progressDialog.show();

                    String name = setname.getText().toString();
                    String email = setemail.getText().toString();
                    String prenom = setprenom.getText().toString();

                    // Créer un objet JSON avec les nouvelles données
                    JSONObject jsonBody = new JSONObject();
                    try {
                        jsonBody.put("id_user", userId);
                        jsonBody.put("nom_user", name);
                        jsonBody.put("prenom_user", prenom);
                        jsonBody.put("email_user", email);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Envoyer les données à l'API
                    sendUserData(jsonBody);
                } else {
                    Toast.makeText(setting.this, "Erreur: ID utilisateur invalide.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendUserData(JSONObject jsonBody) {
        String url = ip+"/Stage/test/update_user.php"; // Remplacer par l'URL de votre API

        // Créer une requête POST
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            boolean success = response.getBoolean("success");
                            String message = response.getString("message");
                            if (success) {
                                Toast.makeText(setting.this, "Données mises à jour avec succès", Toast.LENGTH_SHORT).show();
                                // Vous pouvez rediriger l'utilisateur ou mettre à jour l'interface ici
                            } else {
                                Toast.makeText(setting.this, "Erreur : " + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(setting.this, "Échec de la mise à jour : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Ajouter la requête à la file d'attente
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (data != null) {
                Uri setImageUri = data.getData();
                setprofile.setImageURI(setImageUri);
            }
        }
    }
}

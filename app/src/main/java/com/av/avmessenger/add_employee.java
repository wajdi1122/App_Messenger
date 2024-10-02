package com.av.avmessenger;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;

import com.av.avmessenger.Class.Config;
import com.av.avmessenger.Class.UserData;

import org.json.JSONException;
import org.json.JSONObject;

public class add_employee extends AppCompatActivity {

    String selectedItem;
    EditText nomUser, prenomUser, emailUser, passwordUser, telephoneUser, cinUser, adressUser;
    Button btnAddUser;
    RadioButton admin,emplye;
    RadioGroup radioGroup;
    int type_user = 0;

    private RequestQueue requestQueue; // Déclare la RequestQueue

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        // Initialiser les champs
        nomUser = findViewById(R.id.nom_user);
        prenomUser = findViewById(R.id.prenom_user);
        emailUser = findViewById(R.id.email_user);
        passwordUser = findViewById(R.id.password_user);
        telephoneUser = findViewById(R.id.telephone_user);
        cinUser = findViewById(R.id.cin_user);
        adressUser = findViewById(R.id.adress_user);
        btnAddUser = findViewById(R.id.btn_add_user);
        admin = findViewById(R.id.admin);
        emplye=findViewById(R.id.employee);
        radioGroup=findViewById(R.id.status_group);
        // Initialiser la RequestQueue
        requestQueue = Volley.newRequestQueue(this); // Initialisation de la file de requêtes

        // Configurer le Spinner
        Spinner spinner = findViewById(R.id.spinner_type_user);
        String[] typesUsers = {"Call center", "Digital MOOV", "Elite Training", "Medical Services"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typesUsers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Ne rien faire
            }
        });

        // Configurer le bouton d'ajout d'utilisateur
        btnAddUser.setOnClickListener(v -> {
            String nom = nomUser.getText().toString();
            String prenom = prenomUser.getText().toString();
            String email = emailUser.getText().toString();
            String password = passwordUser.getText().toString();
            String telephone = telephoneUser.getText().toString();
            String cin = cinUser.getText().toString();
            String adresse = adressUser.getText().toString();
            int selectedId = radioGroup.getCheckedRadioButtonId(); // Obtenir l'ID du bouton sélectionné

            if (selectedId != -1) { // Vérifier si un bouton est sélectionné
                if (selectedId == R.id.admin) { // Assurez-vous d'utiliser l'ID correct pour le bouton admin
                    type_user = 1;
                } else if (selectedId == R.id.employee) { // Assurez-vous d'utiliser l'ID correct pour le bouton employe
                    type_user = 2;
                }
            }
            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || password.isEmpty() || telephone.isEmpty() || cin.isEmpty() || adresse.isEmpty()) {
                Toast.makeText(add_employee.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            } else {
                // Envoyer les données à l'API
                UserData userData = new UserData(nom, prenom, email, password, "Employee", telephone, selectedItem, cin, adresse);

                sendUserData(userData,type_user);
            }
        });
    }

    private void sendUserData(UserData userData,int type) {
        String url = Config.BASE_URL+"/Stage/test/ajouter_employee.php"; // Remplacer par l'URL correcte

        // Créer l'objet JSON avec les données de l'utilisateur
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("nom_user", userData.getNom());
            jsonBody.put("prenom_user", userData.getPrenom());
            jsonBody.put("email_user", userData.getEmail());
            jsonBody.put("password_user", userData.getPassword());
            jsonBody.put("status_user", userData.getStatus());
            jsonBody.put("telephone_user", userData.getTelephone());
            jsonBody.put("departement_user", userData.getDepartement());
            jsonBody.put("cin_user", userData.getCin());
            jsonBody.put("adress_user", userData.getAdresse());
            jsonBody.put("type_user", type);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Créer la requête POST
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Gérer la réponse du serveur
                        try {
                            boolean success = response.getBoolean("success");
                            String message = response.getString("message");
                            if (success) {
                                Toast.makeText(add_employee.this, "Utilisateur ajouté avec succès", Toast.LENGTH_SHORT).show();
                                nomUser.setText("");
                                prenomUser.setText("");
                                emailUser.setText("");
                                passwordUser.setText("");
                                telephoneUser.setText("");
                                cinUser.setText("");
                                adressUser.setText("");
                                radioGroup.clearCheck();
                            } else {
                                Toast.makeText(add_employee.this, "Erreur : " + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Gérer les erreurs de la requête
                Toast.makeText(add_employee.this, "Échec de l'appel : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("VolleyError", "Erreur : " + error.toString());
            }
        });

        // Ajouter la requête à la file de requêtes
        requestQueue.add(jsonObjectRequest);
    }
}

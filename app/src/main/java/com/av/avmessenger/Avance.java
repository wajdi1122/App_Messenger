package com.av.avmessenger;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.av.avmessenger.Class.Config;

import org.json.JSONException;
import org.json.JSONObject;

public class Avance extends AppCompatActivity {
    String ip = Config.BASE_URL;
    private EditText raison, montant;
    private Button envoyerButton;
    private RequestQueue requestQueue;
    int userId;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_avance);

        // Initialiser les éléments UI
        raison = findViewById(R.id.label_raison);
        montant = findViewById(R.id.label_montant);
        envoyerButton = findViewById(R.id.button_envoyer);

        // Initialiser la file de requêtes Volley
        requestQueue = Volley.newRequestQueue(this);

        // Récupérer l'id de l'utilisateur depuis l'intent
        userId = getIntent().getIntExtra("userId", -1);

        // Configurer le bouton pour envoyer les données
        envoyerButton.setOnClickListener(v -> {
            // Vérifier que les champs ne sont pas vides
            if (!raison.getText().toString().isEmpty() && !montant.getText().toString().isEmpty()) {
                envoyerDonnees(userId);
            } else {
                Toast.makeText(Avance.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void envoyerDonnees(int userId) {
        String url = ip + "/Stage/test/employee_avance.php";

        // Créer l'objet JSON avec les données
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id_user", userId);
            jsonBody.put("raison", raison.getText().toString());
            jsonBody.put("montant", Integer.parseInt(montant.getText().toString()));
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
                            String status = response.getString("status");
                            String message = response.getString("message");
                            Toast.makeText(Avance.this, message, Toast.LENGTH_SHORT).show();
                            raison.setText("");
                            montant.setText("");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Gérer les erreurs de la requête
                Toast.makeText(Avance.this, "Erreur: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Ajouter la requête à la file de requêtes
        requestQueue.add(jsonObjectRequest);
    }
}

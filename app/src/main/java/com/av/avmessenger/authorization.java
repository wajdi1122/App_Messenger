package com.av.avmessenger;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class authorization extends AppCompatActivity {

    EditText editTextDateDeb, editTextDateFin, raison;
    Calendar calendar;
    String ip = "http://192.168.1.114:8080";
    private RequestQueue requestQueue;
    private Button envoyerButton;
    private int userId;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_autoristation); // Assurez-vous que le nom est correct

        // Initialiser les éléments UI
        raison = findViewById(R.id.editText_raison);
        editTextDateDeb = findViewById(R.id.editText_temps_debut);
        editTextDateFin = findViewById(R.id.editText_temps_fin);
        envoyerButton = findViewById(R.id.button_envoyer);

        // Initialiser la file de requêtes Volley
        requestQueue = Volley.newRequestQueue(this);
        calendar = Calendar.getInstance();

        userId = getIntent().getIntExtra("userId", -1);

        // Sélecteur de date pour Date de Début
        editTextDateDeb.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    authorization.this,
                    (view, year1, month1, dayOfMonth) -> {
                        // Formater la date en yyyy-MM-dd
                        String selectedDate = String.format("%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
                        editTextDateDeb.setText(selectedDate);
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        // Sélecteur de date pour Date de Fin
        editTextDateFin.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    authorization.this,
                    (view, year1, month1, dayOfMonth) -> {
                        // Formater la date en yyyy-MM-dd
                        String selectedDate = String.format("%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
                        editTextDateFin.setText(selectedDate);
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        // Configurer le bouton pour envoyer les données
        envoyerButton.setOnClickListener(v -> {
            if (!raison.getText().toString().isEmpty() &&
                    !editTextDateDeb.getText().toString().isEmpty() &&
                    !editTextDateFin.getText().toString().isEmpty()) {
                envoyerDonnees(userId);
            } else {
                Toast.makeText(authorization.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void envoyerDonnees(int userId) {
        String url = ip + "/Stage/test/employee_autori.php";

        // Créer l'objet JSON avec les données
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id_user", userId);
            jsonBody.put("raison", raison.getText().toString());
            jsonBody.put("date_debut", editTextDateDeb.getText().toString());
            jsonBody.put("date_fin", editTextDateFin.getText().toString());
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
                            Toast.makeText(authorization.this, message, Toast.LENGTH_SHORT).show();
                            raison.setText("");
                            editTextDateDeb.setText("");
                            editTextDateFin.setText("");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Gérer les erreurs de la requête
                Toast.makeText(authorization.this, "Erreur: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Ajouter la requête à la file de requêtes
        requestQueue.add(jsonObjectRequest);
    }
}

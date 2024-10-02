package com.av.avmessenger;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.av.avmessenger.Class.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class List_avance extends AppCompatActivity {

    private RequestQueue requestQueue;
    private RecyclerView recyclerView;
    private AvanceAdapter avanceAdapter;
    private List<Avance> avanceList = new ArrayList<>();
    String ip = Config.BASE_URL;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_avance);

        recyclerView = findViewById(R.id.recyclerViewAvances);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        avanceAdapter = new AvanceAdapter(avanceList);
        recyclerView.setAdapter(avanceAdapter);

        requestQueue = Volley.newRequestQueue(this);
        fetchAvances();
    }

    private void fetchAvances() {
        String url = ip + "/Stage/test/list_avance.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("List_avance", "Réponse JSON: " + response.toString());
                        try {
                            String status = response.getString("status");
                            if (status.equals("success")) {
                                JSONArray data = response.getJSONArray("data");
                                Log.d("List_avance", "Données: " + data.toString());
                                avanceList.clear();
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject avance = data.getJSONObject(i);
                                    int idAvance = avance.getInt("id_avance");
                                    String raison = avance.getString("raison");
                                    String montant = avance.getString("Montant");

                                    // Vérifiez la présence de la clé 'email' avant de l'extraire
                                    String email = avance.getString("email_user");;

                                    avanceList.add(new Avance(idAvance, raison, montant, email));
                                }
                                avanceAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(List_avance.this, "Erreur lors de la récupération des données.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("List_avance", "Erreur: " + error.getMessage());
                Toast.makeText(List_avance.this, "Erreur: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }


    private void sendUpdateRequest(String action, int idAvance) {
        String url = ip + "/Stage/test/" + (action.equals("accepté") ? "accept_avance.php" : "refuse_avance.php");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_avance", idAvance);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if (status.equals("success")) {
                                Toast.makeText(List_avance.this, "Avance " + action + " avec succès.", Toast.LENGTH_SHORT).show();
                                fetchAvances(); // Recharger les données pour refléter les changements
                            } else {
                                Toast.makeText(List_avance.this, "Erreur: " + response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(List_avance.this, "Erreur: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private class AvanceAdapter extends RecyclerView.Adapter<AvanceAdapter.AvanceViewHolder> {

        private List<Avance> avanceList;

        public AvanceAdapter(List<Avance> avanceList) {
            this.avanceList = avanceList;
        }

        @Override
        public AvanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_avance, parent, false);
            return new AvanceViewHolder(view);
        }

        @Override
        public void onBindViewHolder(AvanceViewHolder holder, int position) {
            Avance avance = avanceList.get(position);
            holder.textViewRaison.setText(avance.getRaison());
            holder.textViewMontant.setText(avance.getMontant());
            holder.textViewEmail.setText(avance.getEmail()); // Mise à jour pour afficher l'email

            holder.buttonAccepter.setOnClickListener(v -> {
                // Afficher un message toast pour informer que l'acception est en cours
                Toast.makeText(List_avance.this, "Acception en cours...", Toast.LENGTH_SHORT).show();

                // Créer une nouvelle instance de la requête de mise à jour
                String url = ip+"/Stage/test/accept_avance.php"; // Remplacez par l'URL correcte de votre script PHP

                // Créer un objet JSON avec l'ID de l'avance
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id_avance", avance.getIdAvance());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Créer une nouvelle requête JSON
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    // Vérifier la réponse du serveur
                                    String status = response.getString("status");
                                    if (status.equals("success")) {
                                        Toast.makeText(List_avance.this, "Avance acceptée: " + avance.getRaison(), Toast.LENGTH_SHORT).show();
                                        // Recharger les données ou mettre à jour l'interface utilisateur
                                        fetchAvances(); // Méthode pour recharger les données
                                    } else {
                                        Toast.makeText(List_avance.this, "Erreur: " + response.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(List_avance.this, "Erreur: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                // Ajouter la requête à la file d'attente
                Volley.newRequestQueue(List_avance.this).add(jsonObjectRequest);
            });

            holder.buttonRefuser.setOnClickListener(v -> {
                // Afficher un message toast pour informer que le refus est en cours
                Toast.makeText(List_avance.this, "Refus en cours...", Toast.LENGTH_SHORT).show();

                // Créer une nouvelle instance de la requête de mise à jour
                String url = ip+"/Stage/test/refuse_avance.php"; // Remplacez par l'URL correcte de votre script PHP

                // Créer un objet JSON avec l'ID de l'avance
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id_avance", avance.getIdAvance());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Créer une nouvelle requête JSON
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    // Vérifier la réponse du serveur
                                    String status = response.getString("status");
                                    if (status.equals("success")) {
                                        Toast.makeText(List_avance.this, "Avance refusée: " + avance.getRaison(), Toast.LENGTH_SHORT).show();
                                        // Recharger les données ou mettre à jour l'interface utilisateur
                                        fetchAvances(); // Méthode pour recharger les données
                                    } else {
                                        Toast.makeText(List_avance.this, "Erreur: " + response.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(List_avance.this, "Erreur: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                // Ajouter la requête à la file d'attente
                Volley.newRequestQueue(List_avance.this).add(jsonObjectRequest);
            });

        }

        @Override
        public int getItemCount() {
            return avanceList.size();
        }

        public class AvanceViewHolder extends RecyclerView.ViewHolder {
            TextView textViewRaison, textViewMontant, textViewEmail;
            Button buttonAccepter, buttonRefuser;

            public AvanceViewHolder(View itemView) {
                super(itemView);
                textViewRaison = itemView.findViewById(R.id.textViewRaison);
                textViewMontant = itemView.findViewById(R.id.textViewMontant);
                textViewEmail = itemView.findViewById(R.id.textViewEmail); // Initialisation du TextView pour l'email
                buttonAccepter = itemView.findViewById(R.id.buttonAccepter);
                buttonRefuser = itemView.findViewById(R.id.buttonRefuser);
            }
        }
    }


    private class Avance {
        private int idAvance;
        private String raison;
        private String montant;
        private String email;

        public Avance(int idAvance, String raison, String montant, String email) {
            this.idAvance = idAvance;
            this.raison = raison;
            this.montant = montant;
            this.email = email;
        }

        public int getIdAvance() {
            return idAvance;
        }

        public String getRaison() {
            return raison;
        }

        public String getMontant() {
            return montant;
        }

        public String getEmail() {
            return email;
        }
    }

}

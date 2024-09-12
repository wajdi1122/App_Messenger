package com.av.avmessenger;

import android.os.Bundle;
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
    String ip = "http://192.168.1.114:8080";

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
        String url = ip + "/Stage/test/list_avances.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if (status.equals("success")) {
                                JSONArray data = response.getJSONArray("data");
                                avanceList.clear();
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject avance = data.getJSONObject(i);
                                    int idAvance = avance.getInt("id_avance");
                                    String raison = avance.getString("raison");
                                    String montant = avance.getString("Montant");
                                    avanceList.add(new Avance(idAvance, raison, montant));
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

            holder.buttonAccepter.setOnClickListener(v -> {
                sendUpdateRequest("accepté", avance.getId()); // Envoie une requête pour accepter l'avance
            });

            holder.buttonRefuser.setOnClickListener(v -> {
                sendUpdateRequest("refusé", avance.getId()); // Envoie une requête pour refuser l'avance
            });
        }

        @Override
        public int getItemCount() {
            return avanceList.size();
        }

        public class AvanceViewHolder extends RecyclerView.ViewHolder {
            TextView textViewRaison, textViewMontant;
            Button buttonAccepter, buttonRefuser;

            public AvanceViewHolder(View itemView) {
                super(itemView);
                textViewRaison = itemView.findViewById(R.id.textViewRaison);
                textViewMontant = itemView.findViewById(R.id.textViewMontant);
                buttonAccepter = itemView.findViewById(R.id.buttonAccepter);
                buttonRefuser = itemView.findViewById(R.id.buttonRefuser);
            }
        }
    }

    private class Avance {
        private int id;
        private String raison;
        private String montant;

        public Avance(int id, String raison, String montant) {
            this.id = id;
            this.raison = raison;
            this.montant = montant;
        }

        public int getId() {
            return id;
        }

        public String getRaison() {
            return raison;
        }

        public String getMontant() {
            return montant;
        }
    }
}

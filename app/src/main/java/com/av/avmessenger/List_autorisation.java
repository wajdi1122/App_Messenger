package com.av.avmessenger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.av.avmessenger.Class.Autorisation;
import com.av.avmessenger.Class.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class List_autorisation extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AutorisationAdapter adapter;
    private List<Autorisation> autorisationList = new ArrayList<>();
    private String url = Config.BASE_URL + "/Stage/test/list_autorisation.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_autorisation);

        recyclerView = findViewById(R.id.recyclerViewAutorisation);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AutorisationAdapter(autorisationList, this);
        recyclerView.setAdapter(adapter);

        fetchAutorisations();
    }

    private void fetchAutorisations() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getString("status").equals("success")) {
                            JSONArray data = response.getJSONArray("data");
                            autorisationList.clear();
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject autorisation = data.getJSONObject(i);
                                autorisationList.add(new Autorisation(
                                        autorisation.getInt("id_demande"),
                                        autorisation.getInt("id_user"),
                                        autorisation.getString("raison"),
                                        autorisation.getString("action_dem"),
                                        autorisation.getString("date_debut"),
                                        autorisation.getString("date_fin"),
                                        autorisation.getString("email_user")
                                ));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            showToast("Erreur lors de la récupération des données");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> showToast("Erreur: " + error.getMessage())
        );
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void showToast(String message) {
        Toast.makeText(List_autorisation.this, message, Toast.LENGTH_SHORT).show();
    }
    class AutorisationAdapter extends RecyclerView.Adapter<AutorisationAdapter.ViewHolder> {

        private List<Autorisation> autorisationList;
        private RequestQueue requestQueue;
        private Context context;

        public AutorisationAdapter(List<Autorisation> autorisationList, Context context) {
            this.autorisationList = autorisationList;
            this.context = context;
            this.requestQueue = Volley.newRequestQueue(context);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_autorisation, parent, false);
            return new ViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Autorisation autorisation = autorisationList.get(position);
            holder.raison.setText(autorisation.getRaison());
            holder.email.setText(autorisation.getEmail());
            holder.date.setText("de " + autorisation.getDateDebut() + " jusqu'à " + autorisation.getDateFin());

            holder.buttonAccepter.setOnClickListener(v -> {
                String urlAccept = Config.BASE_URL + "/Stage/test/accepte_autorisation.php";
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id_demande", autorisation.getIdDemande());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlAccept, jsonObject,
                        response -> {
                            try {
                                String status = response.getString("status");
                                if (status.equals("success")) {
                                    Toast.makeText(context, "Demande acceptée: " + autorisation.getRaison(), Toast.LENGTH_SHORT).show();
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(context, "Erreur: " + response.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> Toast.makeText(context, "Erreur: " + error.getMessage(), Toast.LENGTH_SHORT).show());

                requestQueue.add(jsonObjectRequest);
            });

            holder.buttonRefuser.setOnClickListener(v -> {
                String urlRefuse = Config.BASE_URL + "/Stage/test/refuse_autorisation.php";
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id_demande", autorisation.getIdDemande());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlRefuse, jsonObject,
                        response -> {
                            try {
                                String status = response.getString("status");
                                if (status.equals("success")) {
                                    Toast.makeText(context, "Demande refusée: " + autorisation.getRaison(), Toast.LENGTH_SHORT).show();
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(context, "Erreur: " + response.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> Toast.makeText(context, "Erreur: " + error.getMessage(), Toast.LENGTH_SHORT).show());

                requestQueue.add(jsonObjectRequest);
            });
        }

        @Override
        public int getItemCount() {
            return autorisationList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView raison, email, date;
            public Button buttonAccepter, buttonRefuser;

            public ViewHolder(View itemView) {
                super(itemView);
                raison = itemView.findViewById(R.id.textViewRaison);
                email = itemView.findViewById(R.id.textViewEmail);
                date = itemView.findViewById(R.id.textViewdate);
                buttonAccepter = itemView.findViewById(R.id.buttonAccepter);
                buttonRefuser = itemView.findViewById(R.id.buttonRefuser);
            }
        }
    }

}

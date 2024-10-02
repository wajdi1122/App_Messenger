package com.av.avmessenger;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.av.avmessenger.Class.Config;
import com.av.avmessenger.Class.Group;
import com.av.avmessenger.Class.GroupAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class listGrp extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Group> groups;
    private GroupAdapter adapter;
    private RequestQueue requestQueue;
    private SearchView searchView;
    String ip= Config.BASE_URL;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_grp);

        // Initialize ListView and SearchView
        listView = findViewById(R.id.group_list);
        searchView = findViewById(R.id.searchView);
        groups = new ArrayList<>();
        adapter = new GroupAdapter(this, groups);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Group selectedGroup = groups.get(position);
            Intent intent = new Intent(listGrp.this, messageGRP.class);
            int userId = getIntent().getIntExtra("id_user", -1);
            intent.putExtra("senderId", userId); // Pass userId to MessageActivity
            intent.putExtra("groupId", selectedGroup.getId()); // Pass userId to MessageActivity
            intent.putExtra("nom",selectedGroup.getName());
            startActivity(intent);
        });

        // Initialize RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Load groups initially
        loadGroups("");

        // Set up the SearchView to filter groups based on the query
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadGroups(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadGroups(newText);
                return true;
            }
        });
    }

    private void loadGroups(String query) {
        String url = ip+"/Stage/test/load_groupe.php?query=" + query;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    groups.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            int id = jsonObject.getInt("id_groupe");
                            String name = jsonObject.getString("nom_groupes");
                            String imageUrl = jsonObject.getString("img_groupe");
                            groups.add(new Group(id, imageUrl, name));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                },
                error -> {
                    Log.d("errer", error.toString());
                    Toast.makeText(listGrp.this, "Error loading groups: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
        );

        requestQueue.add(jsonArrayRequest);
    }
}
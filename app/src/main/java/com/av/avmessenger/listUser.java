package com.av.avmessenger;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.av.avmessenger.Class.Config;
import com.av.avmessenger.Class.User;
import com.av.avmessenger.Class.UserAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class listUser extends AppCompatActivity {

    private ListView listView;
    private ArrayList<User> users;
    private UserAdapter adapter;
    private RequestQueue requestQueue;
    private SearchView searchView;
    String ip= Config.BASE_URL;
    int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);
        userId = getIntent().getIntExtra("userId", -1);

        // Initialize ListView
        listView = findViewById(R.id.user_list);
        searchView = findViewById(R.id.searchView);
        users = new ArrayList<>();
        adapter = new UserAdapter(this, users);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            User selectedUser = users.get(position);

            Intent intent = new Intent(listUser.this, chatwindo.class);
            Log.d("user66",selectedUser.getFirstName());
            intent.putExtra("userId", userId); // Replace with actual userId
            intent.putExtra("selectedUserId", selectedUser.getId());
            intent.putExtra("nom", selectedUser.getFirstName());
            startActivity(intent);
        });

        // Initialize RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Load all users initially
        loadUsers("");

        // Set up the SearchView to filter users based on the query
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadUsers(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadUsers(newText);
                return true;
            }
        });
    }

    private void loadUsers(String query) {
        String url = ip+"/Stage/test/get_user.php?query=" + query +"&current_user_id="+userId;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    users.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            int id = jsonObject.getInt("id_user");
                            String name = jsonObject.getString("nom_user");
                            String lastname = jsonObject.getString("prenom_user");
                            String imageUrl = jsonObject.getString("img_user");
                            users.add(new User(id, name, lastname, imageUrl));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                },
                error -> {
                    Log.d("errer", error.toString());
                    Toast.makeText(listUser.this, "Error loading users: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
        );

        requestQueue.add(jsonArrayRequest);
    }
}
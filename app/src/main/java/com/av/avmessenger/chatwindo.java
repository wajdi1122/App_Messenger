package com.av.avmessenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.av.avmessenger.Class.Config;
import com.av.avmessenger.Class.Message;
import com.av.avmessenger.Class.MessageAdapter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class chatwindo extends AppCompatActivity {
    private RecyclerView recyclerViewMessages;
    private MessageAdapter messageAdapter;
    private ArrayList<Message> messages;
    private int userId;
    private int selectedUserId;
    private String nom;
    private EditText editTextMessage;
    private CardView buttonSend;
    private TextView recivername;
    String ip= Config.BASE_URL;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatwindo);
        userId = getIntent().getIntExtra("userId", -1);
        selectedUserId = getIntent().getIntExtra("selectedUserId", -1);
        nom = getIntent().getStringExtra("nom");
        Log.d("user55", nom+"");
        recivername=findViewById(R.id.recivername);
        recivername.setText(nom);
        recyclerViewMessages = findViewById(R.id.msgadpter);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messages, userId);
        recyclerViewMessages.setAdapter(messageAdapter);

        editTextMessage = findViewById(R.id.textmsg);
        buttonSend = findViewById(R.id.sendbtnn);

        loadMessages();

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextMessage.getText().toString();
                if (!message.isEmpty()) {
                    sendMessage(userId, selectedUserId, message);
                } else {
                    Toast.makeText(chatwindo.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadMessages() {
        String url = ip + "/Stage/test/get_mess.php?sender=" + userId + "&recipient=" + selectedUserId;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        messages.clear();  // Vide la liste avant de remplir
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int senderId = jsonObject.getInt("sender");
                                String messageText = jsonObject.getString("message");
                                String timestamp = jsonObject.getString("timestamp");
                                String jour = jsonObject.getString("jour");

                                Message message = new Message(senderId, selectedUserId, messageText, timestamp, jour);
                                messages.add(message);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(chatwindo.this, "Error parsing messages", Toast.LENGTH_SHORT).show();
                            }
                        }
                        messageAdapter.notifyDataSetChanged();  // Actualise l'interface utilisateur
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(chatwindo.this, "Error loading messages", Toast.LENGTH_SHORT).show();
                        Log.d("VolleyError", error.toString());
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }


    private void sendMessage(int senderId, int recipientId, String message) {
        new Thread(() -> {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(ip+"/Stage/test/send_msg.php");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoOutput(true);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("message", message);
                jsonObject.put("sender", senderId);
                jsonObject.put("recipient", recipientId);

                try (OutputStream os = urlConnection.getOutputStream()) {
                    byte[] input = jsonObject.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    runOnUiThread(() -> {
                        editTextMessage.setText("");
                        loadMessages();
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(chatwindo.this, "Error sending message", Toast.LENGTH_SHORT).show());
                }

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(chatwindo.this, "Error sending message", Toast.LENGTH_SHORT).show());
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }).start();
    }
}

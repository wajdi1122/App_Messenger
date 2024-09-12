package com.av.avmessenger.Class;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.av.avmessenger.R;

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<User> {

    public UserAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User user = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Lookup view for data population
        ImageView imageViewProfile = convertView.findViewById(R.id.imageViewProfile);
        TextView textViewName = convertView.findViewById(R.id.textViewName);
        TextView textViewLastName = convertView.findViewById(R.id.textViewLastName);

        // Populate the data into the template view using the data object
        // Here you can set image using an image loading library like Glide or Picasso
        // Example: Glide.with(getContext()).load(user.getImageUrl()).into(imageViewProfile);

        textViewName.setText(user.getFirstName());
        textViewLastName.setText(user.getLastName());

        // Return the completed view to render on screen
        return convertView;
    }
}

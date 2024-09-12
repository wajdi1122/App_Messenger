package com.av.avmessenger.Class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.av.avmessenger.R;

import java.util.List;

public class GroupAdapter extends ArrayAdapter<Group> {

    private Context context;
    private List<Group> groups;

    public GroupAdapter(Context context, List<Group> groups) {
        super(context, 0, groups);
        this.context = context;
        this.groups = groups;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.group_list_item, parent, false);
        }

        Group group = getItem(position);
      //  ImageView imageView = convertView.findViewById(R.id.group_image);
        TextView nameView = convertView.findViewById(R.id.group_name);

        // Set image and name (assuming you have a method to load images)
        // imageView.setImageResource(group.getImageResourceId()); // Replace with actual image loading
        nameView.setText(group.getName());

        return convertView;
    }
}

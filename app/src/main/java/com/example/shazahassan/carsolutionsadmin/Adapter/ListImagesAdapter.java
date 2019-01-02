package com.example.shazahassan.carsolutionsadmin.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.shazahassan.carsolutionsadmin.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Shaza Hassan on 16-Oct-18.
 */
public class ListImagesAdapter extends ArrayAdapter<String> {
    private ArrayList<String> links=new ArrayList<>();
    private Context context;
    private int resource;

    public ListImagesAdapter(@NonNull Context context, int resource, ArrayList<String> links) {
        super(context, resource, links);
        this.context = context;
        this.resource = resource;
        this.links = links;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            listItemView = vi.inflate(resource, null);
        }
        String img = getItem(position);
        Log.v("position",img);
        ImageView imageView= listItemView.findViewById(R.id.imageView);
        Picasso.get().load(img).into(imageView);
        return listItemView;
    }
}

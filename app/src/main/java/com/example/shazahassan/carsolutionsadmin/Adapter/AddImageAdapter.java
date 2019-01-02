package com.example.shazahassan.carsolutionsadmin.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.example.shazahassan.carsolutionsadmin.R;

import java.util.ArrayList;

/**
 * Created by Shaza Hassan on 14-Oct-18.
 */
public class AddImageAdapter extends ArrayAdapter<Uri> {

    private Context context;
    private ArrayList<Uri> bitmaps;
    private int resource;
    public AddImageAdapter( Context context, int resource ,ArrayList<Uri> bitmaps) {
        super(context, resource,bitmaps);
        this.context = context;
        this.bitmaps = bitmaps;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            listItemView = vi.inflate(resource, null);
        }
        Uri bitmap = getItem(position);
        if(bitmap != null){
            ImageView imageView = listItemView.findViewById(R.id.image);
            if(imageView != null){
                imageView.setImageURI(bitmap);
            }
            TextView remove = listItemView.findViewById(R.id.remove);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("remove image");
                    builder.setMessage("Would you like to remove image?");

                    // add the buttons
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            bitmaps.remove(position);
                            notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("Cancel", null);

                    // create and show the alert dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
        return listItemView;
    }
}

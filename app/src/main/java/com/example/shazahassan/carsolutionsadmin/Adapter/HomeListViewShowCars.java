package com.example.shazahassan.carsolutionsadmin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shazahassan.carsolutionsadmin.DetailsPage;
import com.example.shazahassan.carsolutionsadmin.Model.GetDataFromDB;
import com.example.shazahassan.carsolutionsadmin.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Shaza Hassan on 16-Oct-18.
 */
public class HomeListViewShowCars extends ArrayAdapter<GetDataFromDB> {
    private Context context;
    private int resource;
    private ArrayList<GetDataFromDB> data = new ArrayList<>();
    public HomeListViewShowCars(@NonNull Context context, int resource, ArrayList<GetDataFromDB> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
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
        final GetDataFromDB getDataFromDB = getItem(position);
        TextView model = listItemView.findViewById(R.id.modelData);
        TextView color = listItemView.findViewById(R.id.colorData);
        TextView contact = listItemView.findViewById(R.id.contactData);
        ImageView carImage = listItemView.findViewById(R.id.car_image);
        TextView noImage = listItemView.findViewById(R.id.no_img);
        model.setText(getDataFromDB.getModel());
        color.setText(getDataFromDB.getColor());
        contact.setText(getDataFromDB.getContactNo());
        if(getDataFromDB.getImages().equals("")){
            carImage.setVisibility(View.INVISIBLE);
        }else {
            Picasso.get().load(getDataFromDB.getImages()).into(carImage);
            noImage.setVisibility(View.GONE);
        }
        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailsPage = new Intent(getContext(),DetailsPage.class);
                detailsPage.putExtra("carID",getDataFromDB.getCarID());
                getContext().startActivity(detailsPage);
            }
        });
        return listItemView;
    }
}

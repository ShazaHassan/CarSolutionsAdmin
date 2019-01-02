package com.example.shazahassan.carsolutionsadmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.shazahassan.carsolutionsadmin.Model.DataForCar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNewCar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DataForCar dataForCar;
    private NavigationView navView;
    private DatabaseReference databaseReference;
    private EditText model, color, chachissNo, importDate, contactNo, moreDetails;
    private String carID;
    private boolean allTrue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_car);
        navView = findViewById(R.id.nav_view);
        navView.setItemIconTintList(null);
        model = findViewById(R.id.model_edit_text);
        color = findViewById(R.id.color_edit_text);
        chachissNo = findViewById(R.id.chachiss_edit_text);
        importDate = findViewById(R.id.import_data_edit_text);
        contactNo = findViewById(R.id.contact_edit_text);
        moreDetails = findViewById(R.id.details_edit_text);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        setupDrawer();
    }

    private void setupDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.add_new_car) {
            startActivity(new Intent(this, AddNewCar.class));
        } else if (id == R.id.home_page) {
            startActivity(new Intent(this, HomePage.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void checkAllData() {
        if (model.getText().toString().equals("")) {
            model.setError("Please enter model of car");
        } else if (color.getText().toString().equals("")) {
            color.setError("please enter color of car");
        } else if (chachissNo.getText().toString().equals("")) {
            chachissNo.setError("please enter chachiss no");
        } else if (contactNo.getText().toString().equals("")) {
            contactNo.setError("enter contact no");
        } else if (importDate.getText().toString().equals("")) {
            importDate.setError("enter import date");
        } else {
            if (moreDetails.getText().toString().equals("")) {
                allTrue = true;
                dataForCar = new DataForCar(model.getText().toString(),
                        color.getText().toString(),
                        chachissNo.getText().toString(),
                        contactNo.getText().toString(),
                        importDate.getText().toString(),
                        "");
            } else {
                allTrue = true;
                dataForCar = new DataForCar(model.getText().toString(),
                        color.getText().toString(),
                        chachissNo.getText().toString(),
                        contactNo.getText().toString(),
                        importDate.getText().toString(),
                        moreDetails.getText().toString());
            }
        }
    }

    private void saveDate() {
        carID = databaseReference.push().getKey();
        dataForCar = new DataForCar(model.getText().toString(),
                color.getText().toString(),
                chachissNo.getText().toString(),
                contactNo.getText().toString(),
                importDate.getText().toString(),
                moreDetails.getText().toString());
        databaseReference.child("Cars").child(carID).setValue(dataForCar).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    public void nextPage(View view) {
        checkAllData();
        if (allTrue) {

            Intent addImage = new Intent(this, AddImage.class);
            addImage.putExtra("car", dataForCar);
            startActivity(addImage);
        }
    }
}

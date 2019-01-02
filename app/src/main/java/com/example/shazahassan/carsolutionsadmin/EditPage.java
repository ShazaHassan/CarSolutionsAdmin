package com.example.shazahassan.carsolutionsadmin;

import android.content.Intent;
import android.os.DeadObjectException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.shazahassan.carsolutionsadmin.Model.DataForCar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditPage extends AppCompatActivity {

    private EditText model, color, chachissNo, importDate, contactNo, moreDetails;
    private String carID;
    private boolean allTrue;
    private DatabaseReference singleCarData;
    private String m,co,ch,con,im,mo,a;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_page);
        Intent intent = getIntent();
        carID = intent.getStringExtra("carID");
        model = findViewById(R.id.model_edit_text_edit);
        color = findViewById(R.id.color_edit_text_edit);
        chachissNo = findViewById(R.id.chachiss_edit_text_edit);
        importDate = findViewById(R.id.import_data_edit_text_edit);
        moreDetails = findViewById(R.id.details_edit_text_edit);
        contactNo = findViewById(R.id.contact_edit_text_edit);
        checkBox = findViewById(R.id.checkAvailability);
        singleCarData = FirebaseDatabase.getInstance().getReference().child("cars").child(carID);
        singleCarData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                m = (String) dataSnapshot.child("model").getValue();
                Log.v("model",m);
                model.setText(m);
                co = (String) dataSnapshot.child("color").getValue();
                color.setText(co);
                ch =  (String) dataSnapshot.child("chachissNo").getValue();
                chachissNo.setText(ch);
                con = (String) dataSnapshot.child("contactNo").getValue();
                contactNo.setText(con);
                im = (String) dataSnapshot.child("importDate").getValue();
                importDate.setText(im);
                mo =(String) dataSnapshot.child("moreDetails").getValue();
                moreDetails.setText(mo);
                Log.v("mo",mo);
                a = (String) dataSnapshot.child("status").getValue();
                if(a.equals("Available")){
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void saveChanges(View view) {
        checkAllData();
        if(allTrue) {
            singleCarData.child("model").setValue(model.getText().toString());
            singleCarData.child("color").setValue(color.getText().toString());
            singleCarData.child("chachissNo").setValue(chachissNo.getText().toString());
            singleCarData.child("contactNo").setValue(contactNo.getText().toString());
            singleCarData.child("importDate").setValue(importDate.getText().toString());
            singleCarData.child("moreDetails").setValue(moreDetails.getText().toString());
            if(checkBox.isChecked()){
                singleCarData.child("status").setValue("Available");
            }else{
                singleCarData.child("status").setValue("Not Available");
            }
            Intent intent = new Intent(this,DetailsPage.class);
            intent.putExtra("carID",carID);
            startActivity(intent);
        }
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
            } else {
                allTrue = true;
            }
        }
    }

}

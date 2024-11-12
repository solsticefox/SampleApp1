package com.example.emptytest1.ui.anotherscreen;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


public class SecondScreenViewModel extends ViewModel {

    //Gets the database instance
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //Gets a reference to that instance
    DatabaseReference myRef = database.getReference();
    private int count = 0;
    //Special data type that has a built in interface that allows for onChange updates
    public MutableLiveData<String> mutatedCount = new MutableLiveData<>("Null");
    public int getCount() {
        return count;
    }

    public void incrementCount() {
        count++;
        myRef.setValue(count*2);
    }


    public void getMutatedCount() {
        //Attaches a listener
        myRef.addValueEventListener(new ValueEventListener() {
            //Does something whenever data is changed
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer value = dataSnapshot.getValue(Integer.class);
                mutatedCount.setValue(value.toString());
            }

            //Does something when something goes wrong
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", "Failed to read value: " + databaseError.getMessage(), databaseError.toException());
            }
        });
    }


}
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
    DatabaseReference myRef = database.getReference("numbers");


    public SecondScreenViewModel() {
        super();
    }
    private int count = 0;
    //Special data type that has a built in interface that allows for onChange updates
    public MutableLiveData<String> mutatedCount = new MutableLiveData<>("Null");
    public int getCount() {
        return count;
    }

    public void resetCount() {
        count = 0;
    }

    public void incrementCount() {
        count++;
    }

    public void sendCount() {
        myRef.push().setValue(count);
    }


    public void getMutatedCount() {
        //Attaches a listener
        myRef.addValueEventListener(new ValueEventListener() {
            //Does something whenever data is changed
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int sum = 0;
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Integer value = childSnapshot.getValue(Integer.class);
                    if (value != null) {
                            sum += value;
                    }
                }
                mutatedCount.setValue(String.valueOf(sum));
            }

            //Does something when something goes wrong
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", "Failed to read value: " + databaseError.getMessage(), databaseError.toException());
            }
        });
    }


}
package com.example.emptytest1.ui.anotherscreen;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.emptytest1.MyApplication;
import com.example.emptytest1.ui.CloudFunctionCaller;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import org.checkerframework.checker.units.qual.C;


public class SecondScreenViewModel extends ViewModel {

    //Gets the database instance
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //Gets a reference to that instance
    DatabaseReference myRef = database.getReference("numbers");
    DatabaseReference mostCommonRef = database.getReference("GreatestOccurence");

    CloudFunctionCaller functionCaller;
    public MutableLiveData<Boolean> isPrime = new MutableLiveData<>();
    public SecondScreenViewModel() {
        super();
        functionCaller = new CloudFunctionCaller(MyApplication.getAppContext());
    }

    public void isPrime() {
        functionCaller.callCloudFunctionAndStoreResult(this);
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

    public void removeFromDB() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshotIndividual: snapshot.getChildren()) {
                    int value = snapshotIndividual.getValue(Integer.class);
                    if (value == count) {
                        String dataID = snapshotIndividual.getKey();
                        myRef.child(dataID).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Delete Data", "Failed deletion", error.toException());
            }
        });
    }

    public void removeAll() {
        myRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("RemoveALL", "Successful remove");
            } else {
                Log.e("RemoveALL", "Failed remove all", task.getException());
            }
        });
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

    public MutableLiveData<String> mostCommonNum = new MutableLiveData<>();
    public MutableLiveData<String> getMostCommonNumFre = new MutableLiveData<>();
    public void mostCommon() {
        //Attaches a listener
        mostCommonRef.addValueEventListener(new ValueEventListener() {
            //Does something whenever data is changed
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.child("number").getValue(String.class);
                String frequency = String.valueOf(dataSnapshot.child("frequency").getValue(Integer.class));
                mostCommonNum.setValue(value);
                getMostCommonNumFre.setValue(frequency);
            }

            //Does something when something goes wrong
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", "Failed to read value: " + databaseError.getMessage(), databaseError.toException());
            }
        });
    }


}
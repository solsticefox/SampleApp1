package com.example.emptytest1.ui.anotherscreen;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.emptytest1.R;

public class SecondScreenFragment extends Fragment {

    //Stores ViewModel
    //ViewModels used to manage items that will be displayed (Does not interact with View elements, but stores data to be put in those views)
    private SecondScreenViewModel countViewModel;

    public static SecondScreenFragment newInstance() {
        return new SecondScreenFragment();
    }

    //onCreateView runs when the view is created by the parent activity
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //Instantiates a ViewModel (We need to instantiate it first before we can reference it
        countViewModel = new ViewModelProvider(requireActivity()).get(SecondScreenViewModel.class);
        //Creates the screen using the xml file instructions
        return inflater.inflate(R.layout.fragment_second_screen, container, false);
    }

    //onViewCreated runs immediately after onCreateView
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //References a button by searching for the element with the correct ID
        Button navigateButton = view.findViewById(R.id.secondToHomeButton);
        //Runs the lambda whenever the button is pressed, typically is an anonymous class, but lambda makes it simpler
        navigateButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_secondScreenFragment_to_homeFragment);
        });
        //Reference the TextView
        TextView count = view.findViewById(R.id.count);
        count.setText(String.valueOf(countViewModel.getCount()));
        TextView mutated = view.findViewById(R.id.mutatedCount);
        //Attaches the listener
        countViewModel.getMutatedCount();
        //Changes something whenever the live data is changed
        countViewModel.mutatedCount.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mutated.setText(s);
            }
        });

        Button increaseCount = view.findViewById(R.id.countIncreaser);
        increaseCount.setOnClickListener(v -> {
            countViewModel.incrementCount();
            count.setText(String.valueOf(countViewModel.getCount()));
        });

        Button resetCount = view.findViewById(R.id.resetButton);
        resetCount.setOnClickListener(v -> {
            countViewModel.resetCount();
            count.setText(String.valueOf(countViewModel.getCount()));
        });

        Button sendToDB = view.findViewById(R.id.addToSum);
        sendToDB.setOnClickListener(v -> {
            countViewModel.sendCount();
        });


    }

}
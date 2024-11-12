package com.example.emptytest1.ui.anotherscreen;

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

    private SecondScreenViewModel countViewModel;

    public static SecondScreenFragment newInstance() {
        return new SecondScreenFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        countViewModel = new ViewModelProvider(requireActivity()).get(SecondScreenViewModel.class);
        return inflater.inflate(R.layout.fragment_second_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button navigateButton = view.findViewById(R.id.secondToHomeButton);
        navigateButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_secondScreenFragment_to_homeFragment);
        });
        TextView count = view.findViewById(R.id.count);
        count.setText(String.valueOf(countViewModel.getCount()));

        Button increaseCount = view.findViewById(R.id.countIncreaser);
        increaseCount.setOnClickListener(v -> {
            countViewModel.incrementCount();
            count.setText(String.valueOf(countViewModel.getCount()));
        });
    }

}
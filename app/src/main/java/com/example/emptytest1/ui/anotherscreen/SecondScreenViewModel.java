package com.example.emptytest1.ui.anotherscreen;

import androidx.lifecycle.ViewModel;

public class SecondScreenViewModel extends ViewModel {
    private int count = 0;
    public int getCount() {
        return count;
    }

    public void incrementCount() {
        count++;
    }


}
package com.psyal5.comp3018_cw1.model;

import android.widget.RelativeLayout;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;

public class BindingAdapters {

    @BindingAdapter("backgroundFromLiveData")
    public static void setBackgroundFromLiveData(RelativeLayout layout, MutableLiveData<Integer> colourLiveData) {
        Integer colour = colourLiveData.getValue();
        if (colour != null) {
            layout.setBackgroundColor(colour);
        }
    }
}

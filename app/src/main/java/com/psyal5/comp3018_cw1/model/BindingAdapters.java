package com.psyal5.comp3018_cw1.model;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

/**
 * BindingAdapters: Contains custom data binding adapters for use in XML layouts.
 */
public class BindingAdapters {

    /**
     * Sets the background colour of a RelativeLayout based on a MutableLiveData of Integer colour.
     *
     * @param layout           The RelativeLayout to set the background colour.
     * @param colourLiveData   MutableLiveData representing the background colour.
     */
    @BindingAdapter("backgroundFromLiveData")
    public static void setBackgroundFromLiveData(RelativeLayout layout, MutableLiveData<Integer> colourLiveData) {
        Integer colour = colourLiveData.getValue();
        if (colour != null) {
            layout.setBackgroundColor(colour);
        }
    }

    /**
     * Sets the music paths in a ListView using a List of music paths.
     *
     * @param listView    The ListView to set the music paths.
     * @param musicPaths   List of music paths to display in the ListView.
     */
    @BindingAdapter("musicPaths")
    public static void setMusicPaths(ListView listView, List<String> musicPaths) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(listView.getContext(), android.R.layout.simple_list_item_1, musicPaths);
        listView.setAdapter(adapter);
    }
}
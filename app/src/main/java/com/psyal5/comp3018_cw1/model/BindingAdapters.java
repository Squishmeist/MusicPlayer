package com.psyal5.comp3018_cw1.model;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.core.util.Consumer;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class BindingAdapters {

    @BindingAdapter("backgroundFromLiveData")
    public static void setBackgroundFromLiveData(RelativeLayout layout, MutableLiveData<Integer> colourLiveData) {
        Integer colour = colourLiveData.getValue();
        if (colour != null) {
            layout.setBackgroundColor(colour);
        }
    }

    @BindingAdapter("app:musicPaths")
    public static void setMusicPaths(ListView listView, List<String> musicPaths) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(listView.getContext(), android.R.layout.simple_list_item_1, musicPaths);
        listView.setAdapter(adapter);
    }

    @BindingAdapter("app:onMusicItemClick")
    public static void setOnMusicItemClick(ListView listView, Consumer<String> onItemClick) {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedMusicUri = ((ArrayAdapter<String>) listView.getAdapter()).getItem(position);
            onItemClick.accept(selectedMusicUri);
        });
    }
}

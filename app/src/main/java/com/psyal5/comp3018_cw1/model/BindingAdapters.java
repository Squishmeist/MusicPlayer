package com.psyal5.comp3018_cw1.model;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.core.util.Consumer;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class BindingAdapters {
    private static final String TAG = "CW1";

    @BindingAdapter("backgroundFromLiveData")
    public static void setBackgroundFromLiveData(RelativeLayout layout, MutableLiveData<Integer> colourLiveData) {
        Integer colour = colourLiveData.getValue();
        if (colour != null) {
            layout.setBackgroundColor(colour);
        }
    }

    @BindingAdapter("musicPaths")
    public static void setMusicPaths(ListView listView, List<String> musicPaths) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(listView.getContext(), android.R.layout.simple_list_item_1, musicPaths);
        listView.setAdapter(adapter);
    }

    @BindingAdapter("onMusicItemSelected")
    public static void setOnMusicItemClick(ListView listView, Consumer<String> onItemClick) {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            ListAdapter adapter = listView.getAdapter();
            if (adapter instanceof ArrayAdapter<?>) {
                String selectedMusicUri = (String) adapter.getItem(position);
                onItemClick.accept(selectedMusicUri);
            } else {
                Log.d(TAG, "adapter is not correct type");
            }
        });
    }
}

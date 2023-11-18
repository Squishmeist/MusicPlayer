package com.psyal5.comp3018_cw1.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.psyal5.comp3018_cw1.R;
import com.psyal5.comp3018_cw1.databinding.ActivitySettingsBinding;
import com.psyal5.comp3018_cw1.viewmodel.SettingsViewModel;

import java.util.Random;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "CW1";
    private SettingsViewModel settingsViewModel;
    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "On create [Settings]");

        ActivitySettingsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        settingsViewModel = new ViewModelProvider(SettingsActivity.this).get(SettingsViewModel.class);
        binding.setSettingsViewModel(settingsViewModel);

        observeViewModel();

        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);

        RelativeLayout rootView = findViewById(R.id.settingsContent);
        rootView.setBackgroundColor(Color.rgb(r, g, b));

    }

    private void observeViewModel() {
        settingsViewModel.getActivity().observe(this, activityClass -> {
            if (activityClass != null) {
                startActivity(new Intent(SettingsActivity.this, activityClass));
            }
        });
    }
}
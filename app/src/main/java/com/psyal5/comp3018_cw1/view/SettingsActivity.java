package com.psyal5.comp3018_cw1.view;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.psyal5.comp3018_cw1.R;
import com.psyal5.comp3018_cw1.databinding.ActivitySettingsBinding;
import com.psyal5.comp3018_cw1.viewmodel.SettingsViewModel;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "CW1";
    private SettingsViewModel settingsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "On create [Settings]");
        ActivitySettingsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        settingsViewModel = new ViewModelProvider(SettingsActivity.this).get(SettingsViewModel.class);
        binding.setSettingsViewModel(settingsViewModel);
        binding.setLifecycleOwner(this);
    }
}
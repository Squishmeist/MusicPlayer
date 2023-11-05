package com.psyal5.comp3018_cw1.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.psyal5.comp3018_cw1.R;
import com.psyal5.comp3018_cw1.databinding.ActivityPlayerBinding;
import com.psyal5.comp3018_cw1.viewmodel.PlayerViewModel;

public class PlayerActivity extends AppCompatActivity {
    private PlayerViewModel playerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityPlayerBinding binding= DataBindingUtil.setContentView(this, R.layout.activity_player);

        // Create an instance of PlayerViewModel
        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        // Set the ViewModel to the binding
        binding.setPlayerViewModel(playerViewModel);
        binding.setLifecycleOwner(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.player);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.main) {
                startActivity(new Intent(PlayerActivity.this, MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }else if (itemId == R.id.player) {
                return true;
            } else if (itemId == R.id.settings) {
                startActivity(new Intent(PlayerActivity.this, SettingsActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else {
                return false;
            }
        });
    }
}
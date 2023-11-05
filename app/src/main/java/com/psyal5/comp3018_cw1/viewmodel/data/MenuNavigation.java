package com.psyal5.comp3018_cw1.viewmodel.data;

import android.app.Activity;
import android.content.Intent;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.psyal5.comp3018_cw1.R;
import com.psyal5.comp3018_cw1.view.MainActivity;
import com.psyal5.comp3018_cw1.view.PlayerActivity;
import com.psyal5.comp3018_cw1.view.SettingsActivity;

public class MenuNavigation {
    public static void setupBottomNavigation(Activity activity) {
        BottomNavigationView bottomNavigationView = activity.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.main) {
                startNewActivity(activity, MainActivity.class);
                return true;
            } else if (itemId == R.id.player) {
                startNewActivity(activity, PlayerActivity.class);
                return true;
            } else if (itemId == R.id.settings) {
                startNewActivity(activity, SettingsActivity.class);
                return true;
            } else {
                return false;
            }
        });
    }

    private static void startNewActivity(Activity activity, Class<?> activityClass) {
        Intent intent = new Intent(activity, activityClass);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }
}
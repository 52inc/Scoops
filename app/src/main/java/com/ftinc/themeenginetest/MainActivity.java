package com.ftinc.themeenginetest;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import com.ftinc.scoop.Scoop;
import com.ftinc.scoop.ui.ScoopSettingsActivity;
import com.ftinc.scoop.util.AttrUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private static final int RC_CHANGE_THEME = 0;

    @BindView(R.id.appbar)
    Toolbar mAppBar;

    @BindView(R.id.daynight_group)
    RadioGroup mDayNightGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);

        // Apply Scoop to the activity
        Scoop.getInstance().apply(this);

        // Set the activity content
        setContentView(R.layout.activity_main);

        // Bind ButterKnife
        ButterKnife.bind(this);

        // Setup Toolbar
        setSupportActionBar(mAppBar);

        int nightMode = AppCompatDelegate.getDefaultNightMode();
        switch (nightMode){
            case AppCompatDelegate.MODE_NIGHT_AUTO:
                mDayNightGroup.check(R.id.daynight_auto);
                break;
            case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
                mDayNightGroup.check(R.id.daynight_system);
                break;
            case AppCompatDelegate.MODE_NIGHT_NO:
                mDayNightGroup.check(R.id.daynight_day);
                break;
            case AppCompatDelegate.MODE_NIGHT_YES:
                mDayNightGroup.check(R.id.daynight_night);
                break;
        }

        mDayNightGroup.setVisibility(Scoop.getInstance().getCurrentFlavor().isDayNight() ? View.VISIBLE : View.GONE);
        mDayNightGroup.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == RC_CHANGE_THEME){
            recreate();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Scoop.getInstance().apply(this, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_settings){
            startActivityForResult(ScoopSettingsActivity.createIntent(this), RC_CHANGE_THEME);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    void onFabClicked(){
        new AlertDialog.Builder(this)
                .setTitle("Dialog")
                .setMessage("Some text explaining this dialog and it's reason for appearance.")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void restart(){
        // Restart activity
        Intent restart = new Intent(this, MainActivity.class);
        restart.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(restart);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.daynight_auto:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
                break;
            case R.id.daynight_system:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case R.id.daynight_day:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case R.id.daynight_night:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }

        recreate();
    }
}

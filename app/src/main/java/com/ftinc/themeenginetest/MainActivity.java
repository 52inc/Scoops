package com.ftinc.themeenginetest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ftinc.scoop.Scoop;
import com.ftinc.scoop.annotations.BindScoop;
import com.ftinc.scoop.ui.ScoopSettingsActivity;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    private static final int[] COLORS = new int[]{
            R.color.amber_500,
            R.color.yellow_500,
            R.color.red_500,
            R.color.blue_500,
            R.color.pink_500,
            R.color.blue_grey_500,
            R.color.green_500,
            R.color.orange_500
    };

    private static final int[] COLORS_DARK = new int[]{
            R.color.amber_800,
            R.color.yellow_800,
            R.color.red_800,
            R.color.blue_800,
            R.color.pink_800,
            R.color.blue_grey_800,
            R.color.green_800,
            R.color.orange_800
    };

    private static final int RC_CHANGE_THEME = 0;

    private final Random random = new Random();

    @BindScoop(Toppings.PRIMARY)
    @BindView(R.id.appbar)
    Toolbar mAppBar;

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

        Scoop.sugarCone()
                .bind(this, Toppings.PRIMARY, mAppBar)
                .bindStatusBar(this, Toppings.PRIMARY_DARK);

        // Setup Toolbar
        setSupportActionBar(mAppBar);
    }

    @Override
    protected void onDestroy() {
        Scoop.sugarCone().unbind(this);
        super.onDestroy();
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
//        new AlertDialog.Builder(this)
//                .setTitle("Dialog")
//                .setMessage("Some text explaining this dialog and it's reason for appearance.")
//                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .show();

        int index = random.nextInt(COLORS.length);
        int randomColor = COLORS[index];
        int randomColorDark = COLORS_DARK[index];
        int color = getResources().getColor(randomColor);
        int colorDark = getResources().getColor(randomColorDark);
        Scoop.sugarCone().update(Toppings.PRIMARY, color);
        Scoop.sugarCone().update(Toppings.PRIMARY_DARK, colorDark);

    }
}

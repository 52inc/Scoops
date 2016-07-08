package com.ftinc.themeenginetest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Switch;

import com.ftinc.scoop.BindTopping;
import com.ftinc.scoop.BindToppingStatus;
import com.ftinc.scoop.Scoop;
import com.ftinc.scoop.ui.ScoopSettingsActivity;
import com.ftinc.themeenginetest.adapters.ButtonColorAdapter;
import com.ftinc.themeenginetest.adapters.CompoundButtonColorAdapter;
import com.ftinc.themeenginetest.adapters.FABColorAdapter;
import com.ftinc.themeenginetest.adapters.ProgressBarColorAdapter;
import com.ftinc.themeenginetest.adapters.RatingBarColorAdapter;
import com.ftinc.themeenginetest.adapters.SeekBarColorAdapter;
import com.ftinc.themeenginetest.adapters.SwitchColorAdapter;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@BindToppingStatus(Toppings.PRIMARY_DARK)
public class MainActivity extends AppCompatActivity {

    /***********************************************************************************************
     *
     * Constants
     *
     */

    private static final int RC_CHANGE_THEME = 0;

    /***********************************************************************************************
     *
     * Variables
     *
     */

    @BindTopping(Toppings.PRIMARY)
    @BindView(R.id.appbar)
    Toolbar mAppBar;

    @BindTopping(
            value = Toppings.ACCENT,
            adapter = FABColorAdapter.class,
            interpolator = AccelerateInterpolator.class
    )
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    @BindTopping(
            value = Toppings.ACCENT,
            adapter = ButtonColorAdapter.class
    )
    @BindView(R.id.button_colored)
    Button mColoredButton;

    @BindTopping(
            value = Toppings.ACCENT,
            adapter = SwitchColorAdapter.class
    )
    @BindView(R.id.switch1)
    Switch mSwitch;

    @BindTopping(
            value = Toppings.ACCENT,
            adapter = CompoundButtonColorAdapter.class
    )
    @BindView(R.id.checkBox)
    CheckBox mCheckBox;

    @BindTopping(
            value = Toppings.ACCENT,
            adapter = CompoundButtonColorAdapter.class
    )
    @BindView(R.id.radioButton)
    RadioButton mRadioButton;

    @BindTopping(
            value = Toppings.ACCENT,
            adapter = RatingBarColorAdapter.class
    )
    @BindView(R.id.ratingBar2)
    RatingBar mRatingBar;

    @BindTopping(
            value = Toppings.ACCENT,
            adapter = SeekBarColorAdapter.class
    )
    @BindView(R.id.seekBar)
    SeekBar mSeekBar;

    @BindTopping(
            value = Toppings.ACCENT,
            adapter = ProgressBarColorAdapter.class
    )
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    /***********************************************************************************************
     *
     * Lifecycle Methods
     *
     */

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

        // Bind Annotations via Code Generation
        Scoop.getInstance().bind(this);

        // Setup Toolbar
        setSupportActionBar(mAppBar);
    }

    @Override
    protected void onDestroy() {
        Scoop.getInstance().unbind(this);
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

    /***********************************************************************************************
     *
     * Listener Methods
     *
     */

    @OnClick({
            R.id.primary1,
            R.id.primary2,
            R.id.primary3,
            R.id.primary4,
            R.id.primary5,
            R.id.primary6
    })
    void onPrimaryColorClicked(View view){
        Button btn = (Button) view;
        int tintColor = btn.getBackgroundTintList().getDefaultColor();

        Scoop.getInstance()
                .update(Toppings.PRIMARY, tintColor);
    }

    @OnClick({
            R.id.dark1,
            R.id.dark2,
            R.id.dark3,
            R.id.dark4,
            R.id.dark5,
            R.id.dark6
    })
    void onPrimaryDarkColorClicked(View view){
        Button btn = (Button) view;
        int tintColor = btn.getBackgroundTintList().getDefaultColor();

        Scoop.getInstance()
                .update(Toppings.PRIMARY_DARK, tintColor);
    }

    @OnClick({
            R.id.accent1,
            R.id.accent2,
            R.id.accent3,
            R.id.accent4,
            R.id.accent5,
            R.id.accent6
    })
    void onAccentColorClicked(View view){
        Button btn = (Button) view;
        int tintColor = btn.getBackgroundTintList().getDefaultColor();

        Scoop.getInstance()
                .update(Toppings.ACCENT, tintColor);
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
}

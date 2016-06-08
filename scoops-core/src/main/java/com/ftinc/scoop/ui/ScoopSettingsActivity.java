package com.ftinc.scoop.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.ftinc.scoop.R;
import com.ftinc.scoop.Scoop;
import com.ftinc.scoop.model.Flavor;
import com.ftinc.scoop.util.AttrUtils;

/**
 * Project: ThemeEngineTest
 * Package: com.ftinc.scoop.ui
 * Created by drew.heavner on 6/8/16.
 */

public class ScoopSettingsActivity extends AppCompatActivity implements FlavorRecyclerAdapter.OnItemClickListener {

    /***********************************************************************************************
     *
     * Intent Factories
     *
     */

    public static Intent createIntent(Context ctx){
        return createIntent(ctx, null);
    }

    public static Intent createIntent(Context ctx, @StringRes int titleResId){
        return createIntent(ctx, ctx.getString(titleResId));
    }

    public static Intent createIntent(Context ctx, @Nullable String title){
        Intent intent = new Intent(ctx, ScoopSettingsActivity.class);
        if(!TextUtils.isEmpty(title)) intent.putExtra(EXTRA_TITLE, title);
        return intent;
    }

    /***********************************************************************************************
     *
     * Constants
     *
     */

    private static final String EXTRA_TITLE = "com.ftinc.scoop.intent.EXTRA_TITLE";

    /***********************************************************************************************
     *
     * Variables
     *
     */

    private Toolbar mAppBar;
    private RecyclerView mRecyclerView;
    private FlavorRecyclerAdapter mAdapter;

    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoop_settings);

        parseExtras(savedInstanceState);
        setupActionBar();
        setupRecyclerView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_TITLE, mTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void parseExtras(Bundle savedInstanceState) {
        if(getIntent() != null){
            mTitle = getIntent().getStringExtra(EXTRA_TITLE);
        }

        if(savedInstanceState != null){
            mTitle = savedInstanceState.getString(EXTRA_TITLE);
        }
    }

    private void setupActionBar() {

        if(getSupportActionBar() == null){
            mAppBar = (Toolbar) findViewById(R.id.appbar);
            setSupportActionBar(mAppBar);

            int toolbarPopupTheme = AttrUtils.getResourceAttr(this, R.attr.toolbarPopupTheme);
            mAppBar.setPopupTheme(toolbarPopupTheme);

            int colorPrimary = AttrUtils.getColorAttr(this, R.attr.colorPrimary);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(colorPrimary));

            mAppBar.setVisibility(View.VISIBLE);
            mAppBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        if(TextUtils.isEmpty(mTitle)) {
            getSupportActionBar().setTitle(R.string.activity_settings);
        }else{
            getSupportActionBar().setTitle(mTitle);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void setupRecyclerView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);

        mAdapter = new FlavorRecyclerAdapter(this);
        mAdapter.setItemClickListener(this);
        mAdapter.addAll(Scoop.getInstance().getFlavors());
        mAdapter.setCurrentFlavor(Scoop.getInstance().getCurrentFlavor());

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onItemClicked(View view, Flavor item, int position) {

        // Update Scoops
        Scoop.getInstance().choose(item);

        // Update adapter
        mAdapter.setCurrentFlavor(item);

        // Restart this activity
        Intent restart = new Intent(this, ScoopSettingsActivity.class);
        finish();
        startActivity(restart);
        overridePendingTransition(0, 0);

    }
}

package com.ftinc.themeenginetest;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ftinc.scoop.Scoop;
import com.ftinc.scoop.model.Flavor;
import com.ftinc.scoop.ui.FlavorRecyclerAdapter;
import com.ftinc.scoop.ui.ScoopSettingsActivity;
import com.ftinc.scoop.util.AttrUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements FlavorRecyclerAdapter.OnItemClickListener {

    @BindView(R.id.appbar)
    Toolbar mAppBar;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

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

        // Setup Recycler
        FlavorRecyclerAdapter adapter = new FlavorRecyclerAdapter(this);
        adapter.addAll(Scoop.getInstance().getFlavors());
        adapter.setItemClickListener(this);

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem settings = menu.findItem(R.id.action_settings);
        settings.getIcon().setTint(AttrUtils.getColorAttr(this, android.R.attr.textColorPrimary));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_settings){
            startActivity(ScoopSettingsActivity.createIntent(this));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(View view, Flavor item, int position) {

        // Update your scoop of ice cream
        Scoop.getInstance().choose(item);

        // Restart activity
        Intent restart = new Intent(this, MainActivity.class);
        restart.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(restart);
        overridePendingTransition(0, 0);
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

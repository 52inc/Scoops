package com.ftinc.themeenginetest;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ftinc.scoop.Scoop;
import com.ftinc.scoop.model.Flavor;
import com.ftinc.themeenginetest.adapter.FlavorRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements FlavorRecyclerAdapter.OnItemClickListener {

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

        // Setup Recycler
        FlavorRecyclerAdapter adapter = new FlavorRecyclerAdapter(this);
        adapter.addAll(Scoop.getInstance().getFlavors());
        adapter.setItemClickListener(this);

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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

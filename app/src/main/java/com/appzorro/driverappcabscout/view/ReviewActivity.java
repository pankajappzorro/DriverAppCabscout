package com.appzorro.driverappcabscout.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.controller.ReviewManager;
import com.appzorro.driverappcabscout.model.AllAdapter.ReviewAdapter;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.Operations;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import dmax.dialog.SpotsDialog;

/**
 * Created by vijay on 14/2/17.
 */

public class ReviewActivity extends AppCompatActivity{
    Toolbar toolbar;
    android.app.AlertDialog dialog;
    RecyclerView recyclerView;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        context =this;
        initviews();

    }

    public void initviews(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Reviews");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = (RecyclerView)findViewById(R.id.listreviws);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

    }
    @Override
    protected void onResume() {
        super.onResume();
        dialog = new SpotsDialog(context);
        dialog.show();
        ModelManager.getInstance().getReviewManager().ReviewManager(context, Operations.reviewofDriver(context, "4"));

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(context);

    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(context);

    }
    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constant.REVIEW:
                dialog.dismiss();
                ReviewAdapter reviewAdapter = new ReviewAdapter(context, ReviewManager.reviewliast);
                recyclerView.setAdapter(reviewAdapter);
                break;

        }
    }
}

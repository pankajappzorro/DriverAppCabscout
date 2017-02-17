package com.appzorro.driverappcabscout.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Date_Time;
import com.appzorro.driverappcabscout.model.Event;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static com.appzorro.driverappcabscout.model.Date_Time.date;

public class RideHistory extends AppCompatActivity {
    MenuItem shareditem;
    Context context;
    String searchdate;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history);
        context=this;
        initviews();


    }
    public void initviews(){
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Ride History");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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
            case Constant.DATE:
               searchdate = date;
                Log.e("searchdate",searchdate);
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.calender, menu);
        shareditem = menu.findItem(R.id.tick);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.tick:
                shareditem.setVisible(false);
                /*dialog = new SpotsDialog(context);
                dialog.show();
                ModelManager.getInstance().getUpdateProfileManager().UpdateProfileManager(context, Config.updateprofileurl, Operations.updateProfile(
                        context, CSPreferences.readString(context,"customer_id"),covertedImage,firstname.getText().toString(),phonenumber.getText().toString()
                ));*/
                break;
            case R.id.calender:
                shareditem.setVisible(true);
                Date_Time date_time = new Date_Time(context);
                date_time.dateDialog();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

}

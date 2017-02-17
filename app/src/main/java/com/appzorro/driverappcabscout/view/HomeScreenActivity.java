package com.appzorro.driverappcabscout.view;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.CustomerReQuestManager;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.model.Beans.CustomerRequest;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Config;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.Operations;
import com.appzorro.driverappcabscout.model.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

public class HomeScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {
    private GoogleMap mMap;
    private static String TAG = HomeScreenActivity.class.getSimpleName();
    Toolbar toolbar;
    private GoogleApiClient mGoogleApiClient;
    DrawerLayout drawerLayout;
    Activity activity = this;
    double source_latitude, source_longitude;

    private float lastSpan = -1;
    private Handler handler = new Handler();
    private ScaleGestureDetector gestureDetector;
    static final int MAX_DURATION = 200;
    double currentlat, currentlang;
     static String request_id,profilepic;

    float zoomLevel;
    Location mLastLocation;
    LatLng latLng;
    private long lastZoomTime = 0;
    TextView timer;
    Dialog requestdialog;

    private int fingers = 0;
    long startTime;

    String location;
    MenuItem shareditem;
    LocationManager locationManager;
    ArrayList<Polyline> polylines;
    private int STORAGE_PERMISSION_CODE = 23;
    private static final int[] COLORS = new int[]{R.color.pathcolor, R.color.colorPrimaryDark, R.color.colorAccent, R.color.colorAccent, R.color.primary_dark_material_light};
    Switch myswitch;
    TextView txtstatus;
    ArrayList<CustomerRequest> list;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        initViews();
    }

    public void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("CABSCOUT");
        setSupportActionBar(toolbar);
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, STORAGE_PERMISSION_CODE);

        initNavigationDrawer();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        myswitch = (Switch) findViewById(R.id.checkbox);
        txtstatus = (TextView) findViewById(R.id.txtstatus);
        if (myswitch.isChecked()) {
            txtstatus.setText("Online");

            // here we call the service of online and offline status of the driver by getting toggle button

            dialog = new SpotsDialog(activity);
            dialog.show();
            ModelManager.getInstance().getOnlineOfflineManager().OnlineOfflineManager(activity, Operations.sendDriverstatus(
                    activity,CSPreferences.readString(activity,"customer_id"),"1"
                    ));

        } else {
            txtstatus.setText("Offline");
        }
        myswitch.setChecked(true);
        myswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    txtstatus.setText("Online");
                    dialog = new SpotsDialog(activity);
                    dialog.show();
                    ModelManager.getInstance().getCustomerReQuestManager().CustomerReQuestManager(activity, Operations.getCustomerRequest(activity, "4"));
                } else {
     // here we send the  the offline status

                    txtstatus.setText("Offline");
                    dialog = new SpotsDialog(activity);
                    dialog.show();
                    ModelManager.getInstance().getOnlineOfflineManager().OnlineOfflineManager(activity, Operations.sendDriverstatus(
                            activity,CSPreferences.readString(activity,"customer_id"),"0"
                    ));

                }

            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;

        }

        mMap.setMyLocationEnabled(true);
        mMap.setOnCameraMoveCanceledListener(new GoogleMap.OnCameraMoveCanceledListener() {
            @Override
            public void onCameraMoveCanceled() {


            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {


            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                return false;
            }
        });
    }

    public void initNavigationDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        View header = navigationView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.driverName);
        ImageView profileimage = (ImageView) header.findViewById(R.id.nav_image);
        TextView phonenumber = (TextView) header.findViewById(R.id.driverContact);
        Log.e("imagedetail", CSPreferences.readString(activity, "user_name") + "\n" + CSPreferences.readString(activity, "profile_pic"));
        name.setText("" + CSPreferences.readString(activity, "user_name"));
        phonenumber.setText("" + CSPreferences.readString(activity, "user_mobile"));
        Picasso.with(this)
                .load(CSPreferences.readString(activity, "profile_pic"))
                .into(profileimage);


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.map:
                Intent intentMap = new Intent(this, HomeScreenActivity.class);
                startActivity(intentMap);
                break;
            case R.id.settings:
                Intent intent = new Intent(activity, Profile_Activity.class);
                startActivity(intent);
                break;
            case R.id.myBooking:
                Intent intent1 = new Intent(activity, MyBookingActivity.class);
                startActivity(intent1);
                break;
            case R.id.rideHistory:
                Intent intHistory = new Intent(this, RideHistory.class);
                startActivity(intHistory);
                break;
            case R.id.review:
                Intent reviewintent = new Intent(this,ReviewActivity.class);
                startActivity(reviewintent);
                break;
            case R.id.logout:
                Intent intLogout = new Intent(this, CabCompanyActivity.class);
                startActivity(intLogout);
                finish();
                CSPreferences.clearPref(activity);
                CSPreferences.putString(activity, "login_status", "false");
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        Log.e("on connected", "");
        initCamera(mLastLocation);
    }
    @Override
    protected void onResume() {

        super.onResume();





    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.


        EventBus.getDefault().unregister(this);
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.disconnect();
    }
// here we are geeting the current location of the user and camera move to cureent location of the user.........

    private void initCamera(Location mLocation) {

        Log.e("init camera", "fuction are called");
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraPosition position = CameraPosition
                .builder().target(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()))
                .zoom(13f)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();
        currentlat = mLocation.getLatitude();
        currentlang = mLocation.getLongitude();

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
        mMap.addMarker(new MarkerOptions().position(new LatLng(currentlat,
                currentlang)));
    }



    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        EventBus.getDefault().register(this);

    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {

            case Constant.CUSTOMERREQUEST:
                dialog.dismiss();
                list =  new ArrayList<>();
                list = CustomerReQuestManager.requestlis;
                final CustomerRequest customerRequest =list.get(0);
                 requestdialog= Utils.createDialog(activity);
                ImageView customerimage = (ImageView)requestdialog.findViewById(R.id.imagevieww);
                TextView  customername = (TextView)requestdialog.findViewById(R.id.txtdrivername);
                final TextView  requestid = (TextView)requestdialog.findViewById(R.id.txtrequestid);
                TextView  distance = (TextView)requestdialog.findViewById(R.id.txtdistance);
                final Button accept =(Button)requestdialog.findViewById(R.id.btnaccept);
                timer = (TextView)requestdialog.findViewById(R.id.txttime);
                timer.setText("00:00");
                String profileimage =customerRequest.getProfilepic();
                Log.e("image ",profileimage);
                TextView  destination = (TextView)requestdialog.findViewById(R.id.txtdestination);
                TextView pickuloaction =(TextView)requestdialog.findViewById(R.id.txtpickaddress);
                requestid.setText(""+customerRequest.getRequestid());
                customername.setText(""+customerRequest.getName());
                Picasso.with(this)
                        .load(profileimage)
                        .into(customerimage);

                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog = new SpotsDialog(activity);
                        dialog.show();
                        ModelManager.getInstance().getAcceptCustomerRequest().AcceptCustomerRequest(activity,Operations.acceptByDriver(activity,
                               CSPreferences.readString(activity,"customer_id"),customerRequest.getRequestid(),
                               String.valueOf(currentlat),String.valueOf(currentlang)));
                    }
                });
               /* destination.setText(""+ Utils.getCompleteAddressString(activity,Double.parseDouble(customerRequest.getDroplat()),
                        Double.parseDouble(customerRequest.getDroplng())));
                pickuloaction.setText(""+Utils.getCompleteAddressString(activity,Double.parseDouble(customerRequest.getSourcelat()),
                        Double.parseDouble(customerRequest.getSourcelng())));*/
                destination.setText("Bilaspur Himachal");
                pickuloaction.setText("Chandigarh");
                final MyTimer timer = new MyTimer(30000, 1000);
                timer.start();
                String pickupaddres = Utils.getCompleteAddressString(activity,Double.parseDouble(customerRequest.getSourcelat()),
                        Double.parseDouble(customerRequest.getSourcelng()));
                Log.e("address",""+pickupaddres);

                requestdialog.show();
                break;
            case Constant.ACCEPTBYDRIVER:
                String message = event.getValue();
                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(message)
                        .setContentText("Your request has been successfully sent to the customer")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Intent intent = new Intent(activity,PathMapActivity.class);
                                Config.startpoint=new LatLng(30.729805,76.769364);
                                Config.endpoint= new LatLng(30.680176,76.745705);
                                startActivity(intent);
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
                break;
            case Constant.DRIVERSTATUS:
                dialog.dismiss();
                String drivermessage = event.getValue();
                if (drivermessage.equals("online")) {
                    txtstatus.setText(drivermessage);

                    new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText(drivermessage)
                            .setContentText("your status are online please wait the customer request")
                            .show();
                }
                else {

                    txtstatus.setText(drivermessage);

                    new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText(drivermessage)
                            .setContentText("your status are offline")
                            .show();
                }
                break;
            case Constant.SERVER_ERROR:
                dialog.dismiss();

                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Error")
                        .setContentText("Your Network connection is very slow please try again")
                        .show();
                break;

        }
    }

/* here we start the timer when customer make a request and driver get
    the reposne of cusomer request if timer is finish then customer
    request send to other driver */

    public class MyTimer  extends CountDownTimer {


        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            long millis = l;
            String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            timer.setText(""+hms);

        }

        @Override
        public void onFinish() {
            timer.setText("00:00");
            requestdialog.dismiss();

        }
    }


}

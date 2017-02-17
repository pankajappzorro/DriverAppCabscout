package com.appzorro.driverappcabscout.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.CustomerReQuestManager;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.model.Beans.CustomerRequest;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Config;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.Operations;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

public class PathMapActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback, RoutingListener, GoogleApiClient.OnConnectionFailedListener {
    GoogleApiClient googleApiClient;
    GoogleMap googleMap;
    ArrayList<Polyline> polylines;
    Context context;
    Location mLastLocation;
    CardView starttriplayout;
    AlertDialog dialog;
    ArrayList<CustomerRequest>list;
    CustomerRequest customerRequest;
    Double lat, lng;
    ImageView callimage,cancelimage,customerimage;
    TextView makecalltext,customername,stoptrips;


    private static final int[] COLORS = new int[]{R.color.pathcolor, R.color.colorPrimaryDark, R.color.colorAccent, R.color.colorAccent, R.color.primary_dark_material_light};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_map);
        context = this;
        initViews();

        makecalltext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                 if (makecalltext.getText().toString().equals("Arrived")){
                     makecalltext.setText("Start");
                     dialog= new SpotsDialog(context);
                     dialog.show();
                     /*ModelManager.getInstance().getArrivedManager().ArrivedManager(context, Operations.arrivedDriver(context,
                             CSPreferences.readString(context,"customer_id"),HomeScreenActivity.request_id));
*/
                     ModelManager.getInstance().getArrivedManager().ArrivedManager(context, Operations.arrivedDriver(context,
                           "4",customerRequest.getRequestid()));
                 }
                else {

                     makecalltext.setText("Arrived");
                     dialog = new SpotsDialog(context);


                     SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss a");
                    Calendar calander = Calendar.getInstance();
                     String currentDate = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
                     Log.e("date and time ",currentDate);
                      String   time = simpleDateFormat.format(calander.getTime());


                     dialog.show();
                     ModelManager.getInstance().getStartTripsManager().StartTripsManager(context,Operations.startTrips(
                             context, CSPreferences.readString(context,"customer_id"),customerRequest.getRequestid(),
                             currentDate+"-"+time,String.valueOf(lat)+","+String.valueOf(lng)
                     ));
                 }

            }
        });

//here we stop the the trips by using click on this button
        stoptrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });
// here we are canceling the trip
        cancelimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });


    }

    public void initViews() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        starttriplayout=(CardView)findViewById(R.id.card1);
        callimage=(ImageView)findViewById(R.id.makecall);
        cancelimage=(ImageView)findViewById(R.id.cancelride);
        customerimage =(ImageView)findViewById(R.id.customerimage);
        customername =(TextView)findViewById(R.id.customername);
        makecalltext=(TextView)findViewById(R.id.txtstart);
        stoptrips=(TextView)findViewById(R.id.stoptrips);
   // here we  get the detail of cutomer whoes make the request using bean class
        list = new ArrayList<>();
        list = CustomerReQuestManager.requestlis;
        customerRequest = list.get(0);
        Picasso.with(this)
                .load(customerRequest.getProfilepic())
                .into(customerimage);
        customername.setText(customerRequest.getName());





    }


    @Override
    protected void onStart() {
        super.onStart();

        googleApiClient.connect();
        EventBus.getDefault().register(context);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        googleApiClient.disconnect();
        EventBus.getDefault().unregister(context);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

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
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        Log.e("on connected", "");
        initCamera(mLastLocation);



    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

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
        googleMap.setMyLocationEnabled(true);

        googleMap.setOnCameraMoveCanceledListener(new GoogleMap.OnCameraMoveCanceledListener() {
            @Override
            public void onCameraMoveCanceled() {

            }
        });
        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {


            }
        });
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener((RoutingListener) context)
                .alternativeRoutes(true)
                .waypoints(Config.startpoint, Config.endpoint)
                .build();
        routing.execute();
    }
    private void initCamera(Location mLocation) {

        Log.e("init camera","fuction are called");
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraPosition position = CameraPosition
                .builder().target(new LatLng(mLocation.getLatitude(),mLocation.getLongitude()))
                .zoom(0f)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();
        lat = mLocation.getLatitude();
        lng = mLocation.getLongitude();

        //googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,
                lng)));
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {

        polylines = new ArrayList<>();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(Config.startpoint);
        builder.include(Config.endpoint);
        LatLngBounds bounds = builder.build();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 300));
        PolylineOptions polyOptions = null;
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }
        int min = 0;
        int indexvalue = 0;

        for (int j = 0; j < arrayList.size(); j++) {
            int colorIndex = j % COLORS.length;
            polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(7 + j * 3);

            if (j > 0) {
                if (min > arrayList.get(j).getDurationValue()) {

                    indexvalue = j;
                    min = arrayList.get(j).getDistanceValue();

                }
            } else {


                min = arrayList.get(j).getDistanceValue();

            }
            Log.e("route", String.valueOf(j + 1) + "     distance--" + arrayList.get(j).getDistanceValue() + "  duration---" + arrayList.get(j).getDurationValue());
            Toast.makeText(getApplicationContext(), "Route " + (j + 1) + ": distance - " + arrayList.get(j).getDistanceValue() + ": duration - " + arrayList.get(j).getDurationValue(), Toast.LENGTH_SHORT).show();
        }
        polyOptions.addAll(arrayList.get(indexvalue).getPoints());
        Polyline polyline = googleMap.addPolyline(polyOptions);
        Log.e("shortest distance", String.valueOf(arrayList.get(indexvalue).getDistanceValue()));
        Log.e("shortest duration", String.valueOf(arrayList.get(indexvalue).getDurationValue()));
        polylines.add(polyline);

        MarkerOptions options = new MarkerOptions();
        options.position(Config.startpoint);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_icon_pin));
        googleMap.addMarker(options);

        // End marker
        options = new MarkerOptions();
        options.position(Config.endpoint);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_icon_pin));
        googleMap.addMarker(options);
        starttriplayout.setVisibility(View.VISIBLE);


      //  googleMap.animateCamera(CameraUpdateFactory.zoomTo(googleMap.getCameraPosition().zoom - 12f));

    }

    @Override
    public void onRoutingCancelled() {

    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {

            case Constant.ARRIVEDSTATUS:
                dialog.dismiss();
                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Arrived")
                        .setContentText("You are arrived on the customer location please make the call to customer")
                        .show();
                break;

            case  Constant.SERVER_ERROR:
                dialog.dismiss();
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Please check your internet connection")
                        .show();
                break;
            case Constant.STARTTRIPS:
                dialog.dismiss();
                starttriplayout.setVisibility(View.GONE);
                stoptrips.setVisibility(View.VISIBLE);
                String message = event.getValue();

                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Started")
                        .setContentText(message)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                googleMap.getUiSettings().setMapToolbarEnabled(true);
                            }
                        })
                        .show();





        }
    }

}

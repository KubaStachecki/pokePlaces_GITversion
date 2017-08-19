package com.example.kuba10.mypokemonplaces.Main;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.kuba10.mypokemonplaces.AddPlaceFragment.AddPlaceFragment;
import com.example.kuba10.mypokemonplaces.FragmentListener;
import com.example.kuba10.mypokemonplaces.ListFragment.ListFragment;
import com.example.kuba10.mypokemonplaces.Model.PokePlace;
import com.example.kuba10.mypokemonplaces.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, MapsContract.View, FragmentListener {

    private GoogleMap mMap;
    private static final int REQUEST_GPS_PERMISSION = 786;
    private FusedLocationProviderClient mFusedLocationClient;
    private double latitude;
    private double longitude;
    SupportMapFragment mapFragment;

    private MapsContract.Presenter mainPresenter;
    FragmentManager fragmentManager;


    private ArrayList<PokePlace> placeList;

    FirebaseDatabase fDatabase;
    DatabaseReference databaseReference;
    DatabaseReference placesRef;
    Date date;


    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.container)
    FrameLayout container;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.fab2)
    FloatingActionButton fab2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo
                .SCREEN_ORIENTATION_PORTRAIT);

        ButterKnife.bind(this);

         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        if (checkPermissions())
            initView();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mainPresenter = new MapsPresenter(this);

        fragmentManager = this.getSupportFragmentManager();
        placeList = new ArrayList<>();



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
                showSnackbar("" + latitude + "  " + longitude);

                AddPlaceFragment.newInstance().show(getSupportFragmentManager(), "");
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openFragment(ListFragment.newInstance(placeList));


            }
        });



    }

    private void initView() {
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getLocation();

        setupDatabase();
        getPlacesList();

    }

    private void setupDatabase() {
        fDatabase = FirebaseDatabase.getInstance();
        databaseReference = fDatabase.getReference();
        placesRef = databaseReference.child("places");
        date = new Date();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (checkPermissions()) {
            mMap.setMyLocationEnabled(true);
        }
        mMap.getUiSettings().setMyLocationButtonEnabled(false);


    }




    public void getLocation() {

        if (checkPermissions()) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null. <- well done google....
                            if (location != null) {

                                LatLng current = new LatLng(location.getLatitude(), location.getLongitude());

                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
                            } else {

                                showSnackbar("GPS problem");

                            }
                        }
                    });



        }
    }

    public void showSnackbar(String text) {

        Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }


    public void getPlacesList() {


        databaseReference.child("places").addValueEventListener(new com.google.firebase.database.ValueEventListener() {

            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                placeList.clear();
                for (com.google.firebase.database.DataSnapshot child : dataSnapshot.getChildren()) {
                    PokePlace pokePlace = child.getValue(PokePlace.class);
                    placeList.add(pokePlace);
                }

                Log.d("LISTA FIREBASE", "   " + placeList.size());
//                showSnackbar("LISTA FIREBASE + " + placeList.size() );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                showSnackbar("Error - check internet connection") ;
                Log.d("LISTA FIREBASE", "nie zadzialala   ");

                showSnackbar("Database error - check connection");

            }
        });
    }


    public void savePlace(PokePlace place) {

        place.setLong(longitude);
        place.setLat(latitude);

        Long id = date.getTime();
        placesRef.child(id.toString()).setValue(place);


    }

    public void openFragment(Fragment fragment) {

        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .add(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermission();

            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_GPS_PERMISSION);
        }
    }


}
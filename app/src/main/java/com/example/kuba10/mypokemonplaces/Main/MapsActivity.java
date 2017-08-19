package com.example.kuba10.mypokemonplaces.Main;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
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

import com.example.kuba10.mypokemonplaces.AddPlaceFragment.AddPlaceFragment;
import com.example.kuba10.mypokemonplaces.FragmentListener;
import com.example.kuba10.mypokemonplaces.ListFragment.ListFragment;
import com.example.kuba10.mypokemonplaces.Model.PokePlace;
import com.example.kuba10.mypokemonplaces.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.kuba10.mypokemonplaces.R.id.map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, MapsContract.View, FragmentListener {

    private static final String TAG = MapsActivity.class.getSimpleName();
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
                .findFragmentById(map);


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

                if (latitude != 0 && longitude != 0) {
                    LatLng current = new LatLng(latitude, longitude);

                    showSnackbar("" + latitude + "  " + longitude);

                    openFragment(AddPlaceFragment.newInstance(current));
                } else {
                    showSnackbar(getString(R.string.unavaliable));
                }


            }
        });

        fab2.setOnClickListener(new View.OnClickListener()

        {
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
        setupDatabase();
        getPlacesList();
        getLocation();
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
        styleMap(googleMap);

        if (checkPermissions()) {
            mMap.setMyLocationEnabled(true);
        } else {
            showSnackbar(getString(R.string.unavaliable));
        }


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng position) {

                openFragment(AddPlaceFragment.newInstance(position));


            }
        });
    }

    private void styleMap(GoogleMap googleMap) {
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setBuildingsEnabled(true);

        try {

            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style1));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
    }


    public void placemarkers() {

        for (PokePlace place : placeList) {

            Log.d("LISTA FIREBASE", "   " + place.getTitle());
            Log.d("LISTA FIREBASE", "   " + place.getLat());
            Log.d("LISTA FIREBASE", "   " + place.getLong());


            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(place.getLat(), place.getLong()))
                    .title(place.getTitle())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer_mid)
));



        }


    }


    public void getLocation() {

        if (checkPermissions()) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {

                                LatLng current = new LatLng(location.getLatitude(), location.getLongitude());

                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 17));

                            } else {

                                showSnackbar(getString(R.string.unavaliable));

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

                placemarkers();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                showSnackbar("Error - check internet connection") ;
                Log.d("LISTA FIREBASE", "database connection error");

                showSnackbar("Database error - check connection");

            }
        });
    }


    public void savePlace(PokePlace place) {

//        place.setLong(longitude);
//        place.setLat(latitude);

        Long id = date.getTime();
        placesRef.child(id.toString()).setValue(place);


    }

    public void openFragment(Fragment fragment) {

        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .add(R.id.coordinator, fragment)
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
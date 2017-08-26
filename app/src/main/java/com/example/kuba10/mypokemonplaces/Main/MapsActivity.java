package com.example.kuba10.mypokemonplaces.Main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.kuba10.mypokemonplaces.AddPlaceFragment.AddPlaceFragment;
import com.example.kuba10.mypokemonplaces.Constants;
import com.example.kuba10.mypokemonplaces.FragmentListener;
import com.example.kuba10.mypokemonplaces.InfoFragment.InfoFragment;
import com.example.kuba10.mypokemonplaces.ListFragment.FirebaseListFragment;
import com.example.kuba10.mypokemonplaces.Model.PokePlace;
import com.example.kuba10.mypokemonplaces.Model.PokemonGo;
import com.example.kuba10.mypokemonplaces.R;
import com.example.kuba10.mypokemonplaces.RESTutils.RetrofitConnection;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.kuba10.mypokemonplaces.R.id.map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, FragmentListener {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private double latitude;
    private double longitude;
    private SupportMapFragment mapFragment;
    private FragmentManager fragmentManager;
    private ArrayList<PokePlace> placesMarkersList;
    private FirebaseDatabase fDatabase;
    private DatabaseReference placesDatabaseReference;
    private ArrayList<PokemonGo> pokemonGo_data_list;
    private RetrofitConnection restDownload;


    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.fragmentFrame)
    FrameLayout fragmentContainer;
    @BindView(R.id.position_fab)
    FloatingActionButton position_fab;
    @BindView(R.id.pikatchu)
    ImageView pikatchuSplash;
    @BindView(R.id.list_fab)
    FloatingActionButton list_fab;
    @BindView(R.id.info_fab)
    FloatingActionButton info_fab;
    @BindView(R.id.sad_pikatchu)
    ImageView pikatchuNoGps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        ButterKnife.bind(this);

        setSplashScreen();

        prepareRESTpokemonData();
        placesMarkersList = new ArrayList<>();

        prepareMapandFragmentManager();

        if (checkPermissions()) {
            initView();
        } else {
            requestPermission();
        }
        setFabListeners();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mapFragment.getView().setVisibility(View.VISIBLE);

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

                openTaggedFragment(AddPlaceFragment.newInstance(position), Constants.ADD_FRAGMENT_TAG);


            }
        });
    }

    private void prepareRESTpokemonData() {
        restDownload = new RetrofitConnection();
        restDownload.downloadPokemonList();
        pokemonGo_data_list = restDownload.getPokemonList();
    }

    private void setFabListeners() {

        position_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
                if (latitude != 0 && longitude != 0) {
                    LatLng current = new LatLng(latitude, longitude);
                    openTaggedFragment(AddPlaceFragment.newInstance(current), Constants.ADD_FRAGMENT_TAG);
                } else {
                    showSnackbar(getString(R.string.unavaliable));
                }
            }
        });

        list_fab.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                openTaggedFragment(FirebaseListFragment.newInstance(), Constants.LIST_FRAGMENT_TAG);
            }
        });

        info_fab.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {


                InfoFragment details = InfoFragment.newInstance();
                details.show(fragmentManager, "");
            }
        });
    }

    private void setSplashScreen() {

        pikatchuNoGps.setVisibility(View.GONE);
        pikatchuSplash.setImageResource(R.drawable.splash_screen_pika);
        pikatchuSplash.setVisibility(View.VISIBLE);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pikatchuSplash.setVisibility(View.GONE);

            }

        }, 3500);
    }

    private void prepareMapandFragmentManager() {

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getView().setVisibility(View.INVISIBLE);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fragmentManager = this.getSupportFragmentManager();
    }

    private void initView() {

        mapFragment.getMapAsync(this);

        if (checkPermissions()) {
            setupDatabase();
            getPlacesList();
            getLocation();
        }


    }

    private void setupDatabase() {
        fDatabase = FirebaseDatabase.getInstance();
        placesDatabaseReference = fDatabase.getReference().child(Constants.PLACES);
    }

    public void placeMarkers() {

        mMap.clear();

        for (PokePlace place : placesMarkersList) {

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

                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(current)
                                        .zoom(18)
                                        .bearing(0)
                                        .tilt(80)
                                        .build();
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                            } else {

                                showSnackbar(getString(R.string.unavaliable));

                            }
                        }
                    });


        }
    }

    public void showSnackbar(String text) {

        Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_LONG)
                .show();
    }

    public void getPlacesList() {

        placesDatabaseReference.addValueEventListener(new com.google.firebase.database.ValueEventListener() {

            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                placesMarkersList.clear();
                for (com.google.firebase.database.DataSnapshot child : dataSnapshot.getChildren()) {
                    PokePlace pokePlace = child.getValue(PokePlace.class);
                    placesMarkersList.add(pokePlace);
                }
                placeMarkers();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showSnackbar(getString(R.string.dataError));

            }
        });
    }

    public void savePlace(PokePlace place) {


        placesDatabaseReference.child(Long.toString(place.getGlobalID())).setValue(place, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    showSnackbar(getString(R.string.placeSavingError));
                } else {
                    showSnackbar(getString(R.string.saveSucess));
                }
            }
        });
    }

    public void openFragment(Fragment fragment) {

        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .add(R.id.fragmentFrame, fragment)
                .addToBackStack(null)
                .commit();

    }

    public void openTaggedFragment(Fragment fragment, String tag) {

        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .add(R.id.fragmentFrame, fragment, tag)
                .addToBackStack(null)
                .commit();

    }

    private boolean checkPermissions() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.REQUEST_GPS_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initView();
            mapFragment.getView().setVisibility(View.VISIBLE);
        } else {

            pikatchuNoGps.setVisibility(View.VISIBLE);
            mapFragment.getView().setVisibility(View.INVISIBLE);
            showSnackbar(getString(R.string.noGpsPermissions));
        }

    }

    public void findPlaceFromList(PokePlace place) {
        Double lat = place.getLat();
        Double lng = place.getLong();
        LatLng selectedPlace = new LatLng(lat, lng);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(selectedPlace)
                .zoom(18)
                .bearing(0)
                .tilt(80)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }

    private void styleMap(GoogleMap googleMap) {

        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setBuildingsEnabled(true);

        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.google_map_style_pokemon));
            if (!success) {
                showSnackbar(getString(R.string.mapStyleError));
            }
        } catch (Resources.NotFoundException e) {
        }

    }

    @Override
    public void dismiss(Fragment fragment) {
        this.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .remove(fragment).commit();
    }

    public void sendDataToAddFragment(PokemonGo pokemon) {
        AddPlaceFragment fragment = (AddPlaceFragment)
                this.getSupportFragmentManager().findFragmentByTag(Constants.ADD_FRAGMENT_TAG);
        fragment.setPokemonId(pokemon);
    }

    public ArrayList<PokemonGo> getPokemonList() {
        if (pokemonGo_data_list == null) {
            restDownload.downloadPokemonList();
            ArrayList<PokemonGo> cleanList = new ArrayList<PokemonGo>();
            return cleanList;
        }
        return restDownload.getPokemonList();
    }

}



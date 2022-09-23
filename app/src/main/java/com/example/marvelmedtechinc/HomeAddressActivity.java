package com.example.marvelmedtechinc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class HomeAddressActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener {
    GoogleMap mGoogleMap;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    SearchView searchView;
    Button button;
    TextView textView;
    String lat,lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_address);
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        searchView = findViewById(R.id.idSearchView);
        button = findViewById(R.id.cont_btn);
        textView = findViewById(R.id.tap_search);

        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        fragment.getMapAsync(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("home_lat",lat);
                hashMap.put("home_lon",lon);
                firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        startActivity(new Intent(HomeAddressActivity.this,HomePageActivity.class));
                        Toast.makeText(HomeAddressActivity.this, "Address Added Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap=googleMap;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mGoogleMap.clear();
                textView.setVisibility(View.GONE);
                button.setVisibility(View.VISIBLE);
                // on below line we are getting the
                // location name from search view.
                String location = searchView.getQuery().toString();

                // below line is to create a list of address
                // where we will store the list of all address.
                List<Address> addressList = null;

                // checking if the entered location is null or not.
                if (location != null || location.equals("")) {
                    // on below line we are creating and initializing a geo coder.
                    Geocoder geocoder = new Geocoder(HomeAddressActivity.this);
                    try {
                        // on below line we are getting location from the
                        // location name and adding that location to address list.
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // on below line we are getting the location
                    // from our list a first position.
                    Address address = addressList.get(0);

                    // on below line we are creating a variable for our location
                    // where we will add our locations latitude and longitude.
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    lat= String.valueOf(address.getLatitude());
                    lon= String.valueOf(address.getLongitude());

                    // on below line we are adding marker to that position.
                    mGoogleMap.addMarker(new MarkerOptions().position(latLng).title("My Location"));

                    // below line is to animate camera to that position.
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mGoogleMap.setOnMapClickListener(HomeAddressActivity.this);
    }

    private void addMarker(LatLng latLng){
        mGoogleMap.addMarker(new MarkerOptions().position(latLng).title("MyLocation"));
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        mGoogleMap.clear();
        addMarker(latLng);
        textView.setVisibility(View.GONE);
        button.setVisibility(View.VISIBLE);
        lat= String.valueOf(latLng.latitude);
        lon= String.valueOf(latLng.longitude);
    }
}
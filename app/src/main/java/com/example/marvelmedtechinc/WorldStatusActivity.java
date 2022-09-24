package com.example.marvelmedtechinc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.spec.MGF1ParameterSpec;

public class WorldStatusActivity extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap mGoogleMap;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_status);
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(googleMap.MAP_TYPE_NORMAL)
                .compassEnabled(false)
                .rotateGesturesEnabled(false)
                .tiltGesturesEnabled(false);
        mGoogleMap = googleMap;

        firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).collection("Location").document("Location").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                double lt = Double.parseDouble("" + value.get("Latitude"));
                double ln = Double.parseDouble("" + value.get("Longitude"));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lt, ln), 12));
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(lt, ln)).title("Your Location"));
            }
        });

        firebaseFirestore.collection("Confinment").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentSnapshot snapshot : value.getDocuments()) {
                    firebaseFirestore.collection("Confinment").document("" + snapshot.get("name")).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            double lt1 = Double.parseDouble("" + value.get("Latitude"));
                            double ln1 = Double.parseDouble("" + value.get("Longitude"));
                            double radius = Double.parseDouble("" + value.get("radius"));

                            mGoogleMap.addCircle(new CircleOptions().center(new LatLng(lt1, ln1)).radius(radius).strokeColor(Color.RED)
                                    .fillColor(Color.rgb(255, 195, 195)).strokeWidth(2));
                        }
                    });
                }
            }
        });

        firebaseFirestore.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentSnapshot snapshot : value.getDocuments()) {
                    firebaseFirestore.collection("Users").document("" + snapshot.get("uid")).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (value.get("virus-test").equals("positive")) {
                                firebaseFirestore.collection("Users").document("" + snapshot.get("uid")).collection("Location").document("Location").addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                        double lt1 = Double.parseDouble("" + value.get("Latitude"));
                                        double ln1 = Double.parseDouble("" + value.get("Longitude"));

                                        mGoogleMap.addCircle(new CircleOptions().center(new LatLng(lt1, ln1)).radius(20).strokeColor(Color.MAGENTA)
                                                .fillColor(Color.MAGENTA));
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
        firebaseFirestore.collection("Vaccination Center").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentSnapshot snapshot : value.getDocuments()) {
                    double lt1 = Double.parseDouble("" + snapshot.get("Latitude"));
                    double ln1 = Double.parseDouble("" + snapshot.get("Longitude"));
                    mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(lt1, ln1)).title("" + snapshot.get("name")).icon(BitmapFromVector(getApplicationContext(), R.drawable.hospital)));
                }
            }
        });
    }

    private BitmapDescriptor BitmapFromVector(Context applicationContext, int call) {
        Drawable vectorDrawable = ContextCompat.getDrawable(applicationContext,call);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
package com.example.marvelmedtechinc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.marvelmedtechinc.databinding.ActivityHomepageBinding;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;


public class HomePageActivity extends AppCompatActivity implements LocationListener {
    ActivityHomepageBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firebaseFirestore;
    LocationManager locationManager;
    GeofencingHelper geofencingHelper;
    GeofenceHelper2 geofenceHelper2;
    private GeofencingClient geofencingClient;
    private GeofencingClient geofencingClient2;
    int a = 0;
    int b = 0;

    int c = 0;
    int d = 0;

    int n=0;

    double lt1, ln1;
    private static final int REQUEST_LOCATION = 1;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        geofencingClient = LocationServices.getGeofencingClient(this);
        geofencingHelper = new GeofencingHelper(this);
        geofenceHelper2 =new GeofenceHelper2(this);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        checkPermissions();

        getMyLocation();
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        }
        binding.consultDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePageActivity.this,ConsultDoctorActivity.class));
            }
        });
        binding.profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePageActivity.this,ProfileActivity.class));
            }
        });

        firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Location startPoint = new Location(auth.getCurrentUser().getUid());
                double lat= Double.parseDouble(""+value.get("home_lat"));
                double lon= Double.parseDouble(""+value.get("home_lon"));
                String v_test=""+value.get("virus-test");
                startPoint.setLatitude(lat);
                startPoint.setLongitude(lon);
                if(v_test.equals("positive")) {
                    firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).collection("Location").document("Location").addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            Location endPoint = new Location(auth.getCurrentUser().getUid());
                            double lat1 = Double.parseDouble("" + value.get("Latitude"));
                            double lon1 = Double.parseDouble("" + value.get("Longitude"));
                            endPoint.setLatitude(lat1);
                            endPoint.setLongitude(lon1);

                            float distance = startPoint.distanceTo(endPoint);
                            if (distance > 500) {
                                NotificationHelper notificationHelper = new NotificationHelper(HomePageActivity.this);
                                notificationHelper.sendHighPriorityNotification("ATTENTION", "As you have been detected positive for Black Virus, it's requested to stay at home for your own and other's safety.", WorldStatusActivity.class);
                            }
                        }
                    });
                }
            }
        });

        if (Build.VERSION.SDK_INT >= 29) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                firebaseFirestore.collection("Confinment").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                            double lt1 = Double.parseDouble("" + snapshot.get("Latitude"));
                            double ln1 = Double.parseDouble("" + snapshot.get("Longitude"));
                            float radius = Float.parseFloat("" + snapshot.get("radius"));
                            Geofence geofence = geofencingHelper.geofence("" + snapshot.get("name"), new LatLng(lt1, ln1), radius, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT);
                            GeofencingRequest geofencingRequest = geofencingHelper.geofencingRequest(geofence);
                            PendingIntent pendingIntent = geofencingHelper.getPendingIntent();

                            if (ActivityCompat.checkSelfPermission(HomePageActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            geofencingClient.addGeofences(geofencingRequest, pendingIntent).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                }
                            });
                        }
                    }
                });
                if(n==0) {
                    firebaseFirestore.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            for (DocumentSnapshot snapshot : value.getDocuments()) {
                                if (snapshot.get("uid") != auth.getCurrentUser().getUid()) {
                                    firebaseFirestore.collection("Users").document("" + snapshot.get("uid")).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                            if (value.get("virus-test").equals("positive")) {
                                                firebaseFirestore.collection("Users").document("" + snapshot.get("uid")).collection("Location").document("Location").addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                                        if (!auth.getCurrentUser().getUid().equals("" + snapshot.get("uid"))) {
                                                            double lt1 = Double.parseDouble("" + value.get("Latitude"));
                                                            double ln1 = Double.parseDouble("" + value.get("Longitude"));

                                                            Geofence geofence = geofenceHelper2.geofence("" + snapshot.get("uid"), new LatLng(lt1, ln1), 100, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT);
                                                            GeofencingRequest geofencingRequest = geofenceHelper2.geofencingRequest(geofence);
                                                            PendingIntent pendingIntent = geofenceHelper2.getPendingIntent();

                                                            if (ActivityCompat.checkSelfPermission(HomePageActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                                                // TODO: Consider calling
                                                                //    ActivityCompat#requestPermissions
                                                                // here to request the missing permissions, and then overriding
                                                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                                //                                          int[] grantResults)
                                                                // to handle the case where the user grants the permission. See the documentation
                                                                // for ActivityCompat#requestPermissions for more details.
                                                                return;
                                                            }
                                                            geofencingClient.addGeofences(geofencingRequest, pendingIntent).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    n=1;
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 120);
            }
        }

        if (b == 0) {
            showData(500);
            b = 1;
        }

        binding.selfAss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePageActivity.this, SelfAssessmentActivity.class));
            }
        });

        binding.virusUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePageActivity.this, VirusUpdatesActivity.class));
            }
        });

        binding.worldStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePageActivity.this, WorldStatusActivity.class));
            }
        });

        binding.vaccinate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePageActivity.this, VaccinationActivity.class));
            }
        });

        binding.a500m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.a500m.setBackgroundResource(R.drawable.grey_back);
                binding.a500m.setAlpha(1);
                binding.a500m.setTextColor(Color.rgb(255, 255, 255));
                binding.a1km.setAlpha((float) 0.7);
                binding.a5km.setAlpha((float) 0.7);
                binding.a10km.setAlpha((float) 0.7);
                binding.a1km.setTextColor(Color.rgb(0, 0, 0));
                binding.a5km.setTextColor(Color.rgb(0, 0, 0));
                binding.a10km.setTextColor(Color.rgb(0, 0, 0));
                binding.a1km.setBackgroundResource(R.drawable.custom_edit);
                binding.a5km.setBackgroundResource(R.drawable.custom_edit);
                binding.a10km.setBackgroundResource(R.drawable.custom_edit);

                showData(500);

            }
        });

        binding.a1km.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.a1km.setBackgroundResource(R.drawable.grey_back);
                binding.a1km.setAlpha(1);
                binding.a1km.setTextColor(Color.rgb(255, 255, 255));
                binding.a500m.setAlpha((float) 0.7);
                binding.a5km.setAlpha((float) 0.7);
                binding.a10km.setAlpha((float) 0.7);
                binding.a500m.setTextColor(Color.rgb(0, 0, 0));
                binding.a5km.setTextColor(Color.rgb(0, 0, 0));
                binding.a10km.setTextColor(Color.rgb(0, 0, 0));
                binding.a500m.setBackgroundResource(R.drawable.custom_edit);
                binding.a5km.setBackgroundResource(R.drawable.custom_edit);
                binding.a10km.setBackgroundResource(R.drawable.custom_edit);

                showData(1000);


            }
        });

        binding.a5km.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.a5km.setBackgroundResource(R.drawable.grey_back);
                binding.a5km.setAlpha(1);
                binding.a5km.setTextColor(Color.rgb(255, 255, 255));
                binding.a1km.setAlpha((float) 0.7);
                binding.a500m.setAlpha((float) 0.7);
                binding.a10km.setAlpha((float) 0.7);
                binding.a1km.setTextColor(Color.rgb(0, 0, 0));
                binding.a500m.setTextColor(Color.rgb(0, 0, 0));
                binding.a10km.setTextColor(Color.rgb(0, 0, 0));
                binding.a1km.setBackgroundResource(R.drawable.custom_edit);
                binding.a500m.setBackgroundResource(R.drawable.custom_edit);
                binding.a10km.setBackgroundResource(R.drawable.custom_edit);

                showData(5000);
            }
        });

        binding.a10km.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.a10km.setBackgroundResource(R.drawable.grey_back);
                binding.a10km.setAlpha(1);
                binding.a10km.setTextColor(Color.rgb(255, 255, 255));
                binding.a1km.setAlpha((float) 0.7);
                binding.a5km.setAlpha((float) 0.7);
                binding.a500m.setAlpha((float) 0.7);
                binding.a1km.setTextColor(Color.rgb(0, 0, 0));
                binding.a5km.setTextColor(Color.rgb(0, 0, 0));
                binding.a500m.setTextColor(Color.rgb(0, 0, 0));
                binding.a1km.setBackgroundResource(R.drawable.custom_edit);
                binding.a5km.setBackgroundResource(R.drawable.custom_edit);
                binding.a500m.setBackgroundResource(R.drawable.custom_edit);

                showData(10000);
            }
        });
        firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.getString("virus-test") == null) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("virus-test", "negative");
                    firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                } else {
                    firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            String v_test = value.getString("virus-test");
                            if (v_test.equals("positive")) {
                                binding.virusTest.setText("positive");
                                binding.virusTest.setTextColor(Color.rgb(244, 81, 30));
                            }
                        }
                    });
                }
                if (value.getString("vaccination") == null) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("vaccination", "Not Vaccinated");
                    firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                } else {
                    firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            String vacc = value.getString("vaccination");
                            if (vacc.equals("Vaccinated")) {
                                binding.vaccination.setText("Vaccinated");
                                binding.vaccination.setTextColor(Color.rgb(49, 70, 26));
                            }
                        }
                    });
                }
            }
        });

    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 110);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 110);
            }
        }
    }

    private void showData(int i) {
        a = 0;
        c = 0;
        d = 0;
        binding.postiveData.setText("0");
        binding.selfTest.setText("0");
        binding.confZone.setText("0");
        firebaseFirestore.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                Location startPoint = new Location(auth.getCurrentUser().getUid());
                for (DocumentSnapshot snapshot : value.getDocuments()) {
                    Location endPoint = new Location(String.valueOf(snapshot.get("uid")));
                    firebaseFirestore.collection("Users").document(String.valueOf(snapshot.get("uid"))).collection("Location").document("Location").addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (String.valueOf(snapshot.get("uid")).equals(auth.getCurrentUser().getUid())) {
                                lt1 = Double.parseDouble("" + value.get("Latitude"));
                                ln1 = Double.parseDouble("" + value.get("Longitude"));
                                startPoint.setLatitude(lt1);
                                startPoint.setLongitude(ln1);
                            } else {
                                double lt2 = Double.parseDouble("" + value.get("Latitude"));
                                double ln2 = Double.parseDouble("" + value.get("Longitude"));
                                endPoint.setLatitude(lt2);
                                endPoint.setLongitude(ln2);
                            }
                            float distance = startPoint.distanceTo(endPoint);
                            String v_test = "" + snapshot.get("virus-test");
                            String self_test = "" + snapshot.get("self_test_result");
                            if (distance <= i && v_test.equals("positive")) {
                                a += 1;
                                binding.postiveData.setText(String.valueOf(a));
                            }
                            if (distance <= i && self_test.equals("positive")) {
                                c += 1;
                                binding.selfTest.setText(String.valueOf(c));
                            }
                        }
                    });
                }
            }
        });

        firebaseFirestore.collection("Confinment").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                d = 0;
                binding.confZone.setText("0");
                Location startPoint = new Location(auth.getCurrentUser().getUid());
                firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).collection("Location").document("Location").addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        lt1 = Double.parseDouble("" + value.get("Latitude"));
                        ln1 = Double.parseDouble("" + value.get("Longitude"));
                        startPoint.setLatitude(lt1);
                        startPoint.setLongitude(ln1);
                    }
                });
                for (DocumentSnapshot snapshot : value.getDocuments()) {
                    Location endPoint = new Location("" + snapshot.get("name"));
                    firebaseFirestore.collection("Confinment").document("" + snapshot.get("name")).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            double c_lat = Double.parseDouble("" + value.get("Latitude"));
                            double c_lon = Double.parseDouble("" + value.get("Longitude"));
                            double radius = Double.parseDouble("" + value.get("radius"));
                            endPoint.setLatitude(c_lat);
                            endPoint.setLongitude(c_lon);

                            float distanceInMeters = (float) (startPoint.distanceTo(endPoint) - radius);

                            if (distanceInMeters <= i) {
                                d += 1;
                                binding.confZone.setText(String.valueOf(d));
                            }
                        }
                    });
                }
            }
        });
    }

    private void getMyLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void OnGPS() {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Enable Location Service").setCancelable(false).setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Latitude", location.getLatitude());
        hashMap.put("Longitude", location.getLongitude());
        firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).collection("Location").document("Location").set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 110) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }
}
package com.example.marvelmedtechinc;

import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;

public class GeofenceHelper2 extends ContextWrapper {
    PendingIntent pendingIntent;

    public GeofenceHelper2(Context base) {
        super(base);
    }

    public GeofencingRequest geofencingRequest(Geofence geofence) {
        return new GeofencingRequest.Builder().addGeofence(geofence)
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .build();
    }

    public Geofence geofence(String ID, LatLng latLng, float radius, int transitionType) {
        return new Geofence.Builder()
                .setCircularRegion(latLng.latitude, latLng.longitude, radius)
                .setRequestId(ID)
                .setTransitionTypes(transitionType)
                .setLoiteringDelay(2000)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
    }

    public PendingIntent getPendingIntent() {
        if (pendingIntent != null) {
            return pendingIntent;
        }
        Intent intent = new Intent(this, PositiveBroadcastReciever.class);
        pendingIntent = PendingIntent.getBroadcast(this, 201, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }
}


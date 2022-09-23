package com.example.marvelmedtechinc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class PositiveBroadcastReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent=GeofencingEvent.fromIntent(intent);

        NotificationHelper notificationHelper=new NotificationHelper(context);

        int trans_type=geofencingEvent.getGeofenceTransition();
        switch (trans_type){
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                notificationHelper.sendHighPriorityNotification("ATTENTION","You are too close to a positive person",WorldStatusActivity.class);
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                Toast.makeText(context, "You are in the Confinement Zone", Toast.LENGTH_SHORT).show();
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                notificationHelper.sendHighPriorityNotification("ALL GOOD","No Positive Person Near You",WorldStatusActivity.class);
                break;
        }
    }
}
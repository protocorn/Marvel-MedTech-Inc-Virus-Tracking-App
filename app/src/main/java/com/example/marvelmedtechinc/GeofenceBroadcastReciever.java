package com.example.marvelmedtechinc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceBroadcastReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        GeofencingEvent geofencingEvent=GeofencingEvent.fromIntent(intent);

        NotificationHelper notificationHelper=new NotificationHelper(context);

        int trans_type=geofencingEvent.getGeofenceTransition();
        switch (trans_type){
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                notificationHelper.sendHighPriorityNotification("ALERT","You Entered A Confinement Zone",WorldStatusActivity.class);
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                notificationHelper.sendHighPriorityNotification("ALL GOOD","You Left The Confinement Zone",WorldStatusActivity.class);
                break;
        }
    }
}
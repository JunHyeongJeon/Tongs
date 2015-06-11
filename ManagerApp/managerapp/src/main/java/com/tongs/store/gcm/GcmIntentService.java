/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tongs.store.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.tongs.store.activity.ClientManagementActivity;

import com.tongs.store.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }
    public static final String TAG = "GCM Demo";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
          //      sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
           //     sendNotification("Deleted messages on server: " + extras.toString());
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                String collapseKey = extras.getString("collapse_key");

                Intent gcmIntent = null;


                Log.i(TAG, "Received: " + extras.toString());

                if( "change".equals(collapseKey) )   {

                    gcmIntent = new Intent(this.getApplicationContext(), ClientManagementActivity.class);
                    gcmIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    gcmIntent.putExtras(extras);
                    startActivity(gcmIntent);
                    Log.v("GCMTEST", collapseKey.toString());
                }
                else if("change".equals(collapseKey))    {
                    Log.v("GCMTEST", collapseKey.toString() );
                }
                else if("change".equals(collapseKey))    {
                    Log.v("GCMTEST", collapseKey.toString() );
                }
                else    {
                    return;
                }




                Intent intent2 = new Intent(this.getApplicationContext(), ClientManagementActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent2.putExtra("data", extras);
                //intent2.setData(Uri.parse(extras.toString()));
                startActivity(intent2);

            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
//    private void sendNotification(String msg, int mode) {
//        final int TICKET = 1;
//        final int COUPON = 2;
//
//        long vibrateTime = 0;
//
//        mNotificationManager = (NotificationManager)
//                this.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        Intent intent = null;
//        PendingIntent contentIntent;
//        if( mode == TICKET ) {   // ticket renew
//            intent = new Intent(this, MainActivity.class);
//            contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
//            vibrateTime = 0;
//        }
//        else if( mode == COUPON )   {           // coupon received
//            intent = new Intent(this, CouponActivity.class);
//            contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
//            vibrateTime = 800;
//        }
//        else    {
//            contentIntent = PendingIntent.getActivity(this, 0,
//                    new Intent(this, SignupActivity.class), 0);
//            vibrateTime = 1000;
//        }
//
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//                | Intent.FLAG_ACTIVITY_NEW_TASK
//                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setContentTitle("고대기")
//                        .setStyle(new NotificationCompat.BigTextStyle()
//                                .bigText(msg))
//                        .setContentText(msg)
//                        .setVibrate(new long[] {0, vibrateTime});
//
//        mBuilder.setContentIntent(contentIntent);
//        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
//    }
}

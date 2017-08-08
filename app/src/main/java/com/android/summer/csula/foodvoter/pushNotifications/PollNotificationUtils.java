package com.android.summer.csula.foodvoter.pushNotifications;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.android.summer.csula.foodvoter.HomeActivity;
import com.android.summer.csula.foodvoter.ListActivity;
import com.android.summer.csula.foodvoter.R;

import java.util.Map;

public class PollNotificationUtils {

    private static final int POLL_NOTIFICATION_ID = 666666;
    private static final int REQUEST_CODE_PENDING_INTENT = 123456;
    private static final int REQUEST_CODE_IGNORE_PENDING_INTENT = 654321;
    private static final String DISMISS_MESSAGE = "DISMISS";
    private static final String QUICK_VOTE_MESSAGE = "Quick-Vote";

    public static final class Data {
        public static final String TITLE = "title";
        public static final String BODY = "body";
        public static final String POLL_ID = "pollId";
    }


    public static void notifyUserOfPollInvites(Context context, Map<String, String> payload) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setLargeIcon(buildLargeIcon(context))
                .setSmallIcon(R.mipmap.food_icon)
                .setContentTitle(payload.get(Data.TITLE))
                .setContentText(payload.get(Data.BODY))
                .setContentIntent(contentIntent(context, payload.get(Data.POLL_ID)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                .addAction(quickVoteNotificationAction(context))
                .addAction(ignoreNotificationAction(context))
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(POLL_NOTIFICATION_ID, notificationBuilder.build());
    }


    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    private static PendingIntent contentIntent(Context context, String pollId) {
        Intent startHomeActivityIntent = ListActivity.newIntent(context, pollId);
        return PendingIntent.getActivity(
                context,
                REQUEST_CODE_PENDING_INTENT,
                startHomeActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    private static NotificationCompat.Action ignoreNotificationAction(Context context) {
        Intent ignoreNotificationIntent = new Intent(context, VoteIntentService.class);
        ignoreNotificationIntent.setAction(VoteTask.ACTION_DISMISS_NOTIFICATIONS);

        PendingIntent ignoreNotificationPendingIntent = PendingIntent.getService(
                context,
                REQUEST_CODE_IGNORE_PENDING_INTENT,
                ignoreNotificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        return new NotificationCompat.Action(
                R.drawable.ic_cancel,
                DISMISS_MESSAGE,
                ignoreNotificationPendingIntent);
    }

    private static NotificationCompat.Action quickVoteNotificationAction(Context context) {
        Intent ignoreNotificationIntent = new Intent(context, VoteIntentService.class);
        ignoreNotificationIntent.setAction(VoteTask.ACTION_DISMISS_NOTIFICATIONS);

        PendingIntent ignoreNotificationPendingIntent = PendingIntent.getService(
                context,
                REQUEST_CODE_IGNORE_PENDING_INTENT,
                ignoreNotificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        return new NotificationCompat.Action(
                R.drawable.ic_motorcycle,
                QUICK_VOTE_MESSAGE,
                ignoreNotificationPendingIntent);
    }

    private static Bitmap buildLargeIcon(Context context) {
        Resources resources = context.getResources();
        return BitmapFactory.decodeResource(resources, R.mipmap.food_icon);
    }
}

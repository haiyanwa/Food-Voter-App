package com.android.summer.csula.foodvoter.pushNotifications;


import android.content.Context;

public class VoteTask {

    private static final String TAG = VoteTask.class.getSimpleName();

    public static final String ACTION_DISMISS_NOTIFICATIONS = "dismiss_notification";
    public static final String ACTION_QUICK_VOTE = "quick_vote";


    public static void executeTask(Context context, String action) {
        switch (action) {
            case ACTION_DISMISS_NOTIFICATIONS:
                PollNotificationUtils.clearAllNotifications(context);
                break;
            case ACTION_QUICK_VOTE:
                PollNotificationUtils.clearAllNotifications(context);
                break;
        }
    }


}

package com.android.summer.csula.foodvoter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.summer.csula.foodvoter.database.FoodVoterFirebaseDb;
import com.android.summer.csula.foodvoter.database.UserUpdater;
import com.android.summer.csula.foodvoter.models.User;
import com.android.summer.csula.foodvoter.polls.AllPollsFragment;
import com.android.summer.csula.foodvoter.polls.InvitedToPollFragment;
import com.android.summer.csula.foodvoter.polls.PollActivity;
import com.android.summer.csula.foodvoter.pushNotifications.MyFirebasePreference;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class HomeActivity extends AppCompatActivity implements FoodVoterFirebaseDb.Listener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final int REQUEST_CODE_SIGN_IN = 1;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference connectedDatabaseReference;
    private ValueEventListener connectedValueListener;
    private FoodVoterFirebaseDb database;

    private FirebaseUser firebaseUser;

    private TextView usernameTextView;
    private ImageView userPresenceImage;
    private TabLayout tabLayout;
    private PollBroadcastReceiver pollBroadcastReceiver;
    private IntentFilter pollIntentFilter;


    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.d(TAG, "token => " + MyFirebasePreference.getToken(this));

        /* Setup firebase database */
        connectedDatabaseReference = FirebaseDatabase.getInstance().getReference(".info/connected");

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = setupAuthStateListener();

        usernameTextView = (TextView) findViewById(R.id.tv_username);
        userPresenceImage = (ImageView) findViewById(R.id.image_view_my_presence);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout_home_polls);

        FloatingActionButton addPollButton = (FloatingActionButton) findViewById(R.id.button_add_poll);
        addPollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User(firebaseUser.getDisplayName(), firebaseUser.getUid(), true);
                user.setToken(MyFirebasePreference.getToken(HomeActivity.this));
                startActivity(PollActivity.newIntent(HomeActivity.this, user));
            }
        });


        // Setup up broadcast receiver so it can notify the activity when poll write are done
        pollIntentFilter = new IntentFilter();
        pollIntentFilter.addAction(PollBroadcastReceiver.ACTION_SUCCESSFUL_WRITE);
        pollIntentFilter.addAction(PollBroadcastReceiver.ACTION_UNSUCCESSFUL_WRITE);
        pollBroadcastReceiver = new PollBroadcastReceiver();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
        // Since the fragment data could change during onPause, we need to (re) initialized the tab
        // layout during onResume();
        initializeTabLayout();

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(pollBroadcastReceiver, pollIntentFilter);
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
        detachDatabaseReadListener();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(pollBroadcastReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult()");
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                String displayName = firebaseAuth.getCurrentUser().getDisplayName();
                Toast.makeText(this, "Signed in as " + displayName, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int selectedItemId = item.getItemId();

        switch (selectedItemId) {
            case R.id.friends_menu:
                launchFriendsActivity();
                return true;
            case R.id.sign_out_menu:
                setUserOnlineStatus(false);
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private FirebaseAuth.AuthStateListener setupAuthStateListener() {
        return new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser != null) {
                    onSignedInInitialized();
                } else {
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(
                                            Arrays.asList(
                                                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build())
                                    ).build(),
                            REQUEST_CODE_SIGN_IN);
                }
            }
        };
    }

    private void onSignedInInitialized() {
        database = new FoodVoterFirebaseDb(this, firebaseUser.getUid());
        usernameTextView.setText(firebaseUser.getDisplayName());
        logUserOnline();
        attachDatabaseReadListener();
        attachConnectedValueListener();
    }

    private void initializeTabLayout() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout_home_polls);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                swapFragment(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_poll_list, AllPollsFragment.newInstance())
                .commit();
    }

    private void swapFragment(TabLayout.Tab tab) {
        String selectedTab = tab.getText().toString();
        String allTabs = getResources().getString(R.string.tab_item_all_polls);
        String invitedTab = getResources().getString(R.string.tab_item_invited_polls);

        if (selectedTab.equals(allTabs)) {
            replaceFragment(AllPollsFragment.newInstance());
        } else if (selectedTab.equals(invitedTab)) {
            replaceFragment(InvitedToPollFragment.newInstance(firebaseUser.getUid()));
        }
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_poll_list, fragment)
                .commit();
    }

    /**
     * Log user online. If the user is new, add them to the database first.
     * Update their token on login.
     */
    private void logUserOnline() {
        User authenticatedUser = new User(firebaseUser.getDisplayName(), firebaseUser.getUid());
        UserUpdater.logUserOnline(this, authenticatedUser); // won't logUserOnline existing users
    }

    private void attachDatabaseReadListener() {
        database.attachReadListener();
    }

    private void attachConnectedValueListener() {
        connectedValueListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isConnected = dataSnapshot.getValue(Boolean.class);

                if (isConnected) {
                    userPresenceImage.setImageResource(android.R.drawable.presence_online);
                } else {
                    userPresenceImage.setImageResource(android.R.drawable.presence_offline);
                }

                setUserOnlineStatus(isConnected);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        connectedDatabaseReference.addValueEventListener(connectedValueListener);
    }

    private void onSignedOutCleanup() {
        usernameTextView.setText("");
        detachDatabaseReadListener();
    }

    private void detachDatabaseReadListener() {
        if (database != null) {
            database.detachReadListener();
        }

        if (connectedDatabaseReference != null && connectedValueListener != null) {
            connectedDatabaseReference.removeEventListener(connectedValueListener);
        }
    }

    private void launchFriendsActivity() {
        String userId = firebaseUser.getUid();
        Context context = this;
        Intent friendsIntent = FriendsActivity.newIntent(context, userId);
        startActivity(friendsIntent);
    }

    private void setUserOnlineStatus(final boolean isOnline) {
        UserUpdater.updateOnlineStatus(firebaseUser.getUid(), isOnline);
    }

    @Override
    public void onUserAdded(User user) { } // Left intentionally blank

    @Override
    public void onUserChanged(User user) { } // Left intentionally blank

    @Override
    public void onFriendAdded(User user) { } // Left intentionally blank


    /**
     * This broadcast receiver is use to let us know if writing Poll object to Firebase Real Time
     * Database was successful or not. It will show a toast to alert the users of the status.
     */
    public class PollBroadcastReceiver extends BroadcastReceiver {
        public static final String ACTION_SUCCESSFUL_WRITE = "successful_create";
        public static final String ACTION_UNSUCCESSFUL_WRITE = "unsuccessful_create";


        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            switch (action) {
                case ACTION_SUCCESSFUL_WRITE:
                    displaySuccessfulWriteToast(context);
                    break;
                case ACTION_UNSUCCESSFUL_WRITE:
                    displayUnsuccessfulWriteToast(context);
                    break;
            }
        }

        private void displaySuccessfulWriteToast(Context context) {
            Toast.makeText(context, "Poll Creation was successful!", Toast.LENGTH_LONG).show();
        }

        private void displayUnsuccessfulWriteToast(Context context) {
            Toast.makeText(context, "Poll Creation was successful!", Toast.LENGTH_LONG).show();
        }
    }
}

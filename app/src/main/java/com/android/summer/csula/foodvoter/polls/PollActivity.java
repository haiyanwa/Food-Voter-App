package com.android.summer.csula.foodvoter.polls;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.summer.csula.foodvoter.R;
import com.android.summer.csula.foodvoter.models.User;
import com.android.summer.csula.foodvoter.polls.models.Poll;
import com.android.summer.csula.foodvoter.yelpApi.models.Coordinate;
import com.android.summer.csula.foodvoter.yelpApi.models.YelpPriceLevel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class PollActivity extends AppCompatActivity implements
        SettingFragment.OnPollSettingsListener, InvitedVotersFragment.OnPollInvitesListener {

    private static final String TAG = PollActivity.class.getSimpleName();
    private static final String EXTRA_USER = "user";
    private static final String FRAGMENT_SETTING_TAG = "setting";
    private static final String FRAGMENT_INVITES_TAG = "invites";
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 12345;

    private Poll poll;

    private FusedLocationProviderClient fusedLocationClient;
    private Coordinate coordinate;

    public PollActivity() {}


    public static Intent newIntent(Context context, User user) {
        Intent intent = new Intent(context, PollActivity.class);
        intent.putExtra(EXTRA_USER, user);
        return intent;
    }

    private static User getUserFromIntent(Intent intent) {
        return (User) intent.getSerializableExtra(EXTRA_USER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_configuration);

        User user = getUserFromIntent(getIntent());
        Log.d(TAG, "onCreate => user.toString => " + user.toString());

        // This poll doesn't have an id yet, the id is inserted in later when the user click
        // on the submit button. The poll insertion is done in the PollIntentService
        poll = new Poll(user);

        // used to obtain app user location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        initializeUI();

        // Having location permission will lead to a UI change so we have to request it after
        // the UI is initialized
        requestPermissionAccessLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            // When we are granted location permission, go unlock the "current location" radio button
            case REQUEST_CODE_PERMISSION_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateFragmentOnLocationSuccess();
                } else {
                    poll.setCoordinate(null);
                    displayNoLocationToast();
                }
        }
    }

    /**
     * Unlock the "current_location" radio button if we have permission to access the user's location.
     * If we don't, then we will ask.
     */
    private void requestPermissionAccessLocation() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        Log.d(TAG, "permissionCheck: " + permissionCheck);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            updateFragmentOnLocationSuccess();
        } else {
            ActivityCompat
                    .requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_CODE_PERMISSION_LOCATION);
        }
    }

    /**
     * Communicate with the settings fragment to unlock the "current_location" button
     */
    private void updateFragmentOnLocationSuccess() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        coordinate = new Coordinate(location.getLatitude(), location.getLongitude());
                        poll.setCoordinate(coordinate);

                        // Obtain the setting fragment so we can communicate with it through
                        // public methods
                        SettingFragment settingFragment = (SettingFragment) getSupportFragmentManager()
                                .findFragmentByTag(FRAGMENT_SETTING_TAG);

                        if (settingFragment != null) {
                            settingFragment.unlockCurrentLocationRadio(true);
                        }
                    } else {
                        displayNoLocationToast();
                    }
                }
            });
        }
    }

    private void initializeUI() {
        initTabLayout();
        initSubmitButton();
    }

    private void initTabLayout() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_poll);
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

        // Use setting as the default tab, the Poll object should be mostly null values
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.poll_fragment_container, SettingFragment.newInstance(poll), FRAGMENT_SETTING_TAG)
                .commit();

    }

    private void initSubmitButton() {
        Button submit = (Button) findViewById(R.id.button_submit_poll);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startPollIntentService();
                } catch (Exception e) {
                    e.printStackTrace();
                    displayPollErrorToast();
                } finally {
                    finish();
                }
            }
        });
    }

    private void startPollIntentService() {
        Intent intent = PollIntentService.newIntent(PollActivity.this, poll);
        startService(intent);
    }

    private void swapFragment(TabLayout.Tab tab) {
        String selectedTab = tab.getText().toString();
        String settings = getResources().getString(R.string.tab_item_settings);
        String invites = getResources().getString(R.string.tab_item_invites);

        if (selectedTab.equals(settings)) {
            replaceFragment(SettingFragment.newInstance(poll), FRAGMENT_SETTING_TAG);
            updateFragmentOnLocationSuccess();
        } else if (selectedTab.equals(invites)) {
            replaceFragment(InvitedVotersFragment.newInstance(poll), FRAGMENT_INVITES_TAG);
        }
    }

    private void replaceFragment(Fragment fragment, String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.poll_fragment_container, fragment, fragmentTag)
                .commit();

    }


    private void displayPollErrorToast() {
        String msg = "Something went wrong, try again";
        Toast.makeText(PollActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    private void displayNoLocationToast() {
        Toast.makeText(PollActivity.this,
                       "I can't detect your location, please enter it manually!",
                       Toast.LENGTH_LONG).show();
    }

    /*  These methods are used for Fragment to Activity communications */

    @Override
    public void onTitleChange(String title) {
        poll.setTitle(title);
    }

    @Override
    public void onDescriptionChange(String description) {
        poll.setDescription(description);
    }

    @Override
    public void onOpenNowChange(boolean openNow) {
        poll.setOpenNow(openNow);
    }

    @Override
    public void onPriceChange(String price) {
        // Price would be in Yelp's price level format ($, $$, $$$, $$$$) but the API
        // uses integer (1,2,3,4) instead. We convert it to an intge String, a value that Yelp
        // api requires
        poll.setPrice(YelpPriceLevel.fromYelpString(price));
    }

    @Override
    public void onZipCodeChange(String zipCode) {
        poll.setZipCode(zipCode);
    }

    @Override
    public void onUseCoordinate(boolean useCoordinate) {
        // We either use zip code as our location to OR the geolocation to make an API call to Yelp,
        // so we set one of to null to know which one to use. We check coordinte first, then zipCode

        if (useCoordinate) {
            poll.setZipCode(null);
            poll.setCoordinate(coordinate);
        } else {
            poll.setCoordinate(null);
        }
    }

    @Override
    public void onUserInvited(User voter, boolean invited) {
        if (invited) {
            poll.addVoters(voter);
        } else {
            poll.removeVoters(voter);
        }
    }
}

package com.android.summer.csula.foodvoter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.android.summer.csula.foodvoter.adapters.UsersAdapter;
import com.android.summer.csula.foodvoter.database.FoodVoterFirebaseDb;
import com.android.summer.csula.foodvoter.models.User;

import static android.support.v7.widget.RecyclerView.*;

public class FriendsActivity extends AppCompatActivity implements
        UsersAdapter.UserAdapterListener,
        FoodVoterFirebaseDb.Listener {

    private static final String EXTRA_USER_ID = "user_id";
    private static final String TAG = FriendsActivity.class.getSimpleName();

    private RecyclerView friendsRecyclerView;
    private UsersAdapter usersAdapter;
    private FloatingActionButton addFriendButton;

    private FoodVoterFirebaseDb foodVoterFirebaseDb;

    private String userId;

    public static Intent newIntent(Context context, String userId) {
        Intent friendsIntent = new Intent(context, FriendsActivity.class);
        friendsIntent.putExtra(EXTRA_USER_ID, userId);
        return friendsIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        initialize();
    }

    private void initialize() {
        initUserId();
        initViews();
    }

    private void initUserId() {
        Intent currentIntent = getIntent();
        userId = currentIntent.getStringExtra(EXTRA_USER_ID);
    }

    private void initDatabase() {
        foodVoterFirebaseDb = new FoodVoterFirebaseDb(this, userId);
        foodVoterFirebaseDb.attachReadListener();
    }

    private void initViews() {
        LayoutManager layoutManager = new LinearLayoutManager(this);

        usersAdapter = new UsersAdapter(this, R.drawable.ic_remove_circle);

        friendsRecyclerView = (RecyclerView) findViewById(R.id.rv_friend_list);
        friendsRecyclerView.setLayoutManager(layoutManager);
        friendsRecyclerView.setAdapter(usersAdapter);

        addFriendButton = (FloatingActionButton) findViewById(R.id.btn_add_friend);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAddFriendsActivity = AddFriendshipActivity.newIntent(
                        FriendsActivity.this, userId);
                startActivity(intentAddFriendsActivity);
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                User friend = (User) viewHolder.itemView.getTag();
                Log.d(TAG, "swipe friend: " + friend.toString());
                foodVoterFirebaseDb.unfriendUser(friend);
                usersAdapter.remove(friend);

            }
        }).attachToRecyclerView(friendsRecyclerView);
    }

    @Override
    public void onFriendAdded(User friend) {
        usersAdapter.add(friend);
    }

    @Override
    public void onUserClick(User user) {
        foodVoterFirebaseDb.unfriendUser(user);
        usersAdapter.remove(user);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDatabase();
    }

    @Override
    protected void onPause() {
        super.onPause();
        foodVoterFirebaseDb.detachReadListener();
        usersAdapter.clear();
    }

    @Override
    public void onUserAdded(User user) { } // Do nothing

    @Override
    public void onUserChanged(User user) {
        usersAdapter.updateOnlineStatus(user);
    }
}

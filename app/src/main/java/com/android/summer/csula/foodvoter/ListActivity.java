package com.android.summer.csula.foodvoter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.summer.csula.foodvoter.models.Vote;
import com.android.summer.csula.foodvoter.polls.models.Poll;
import com.android.summer.csula.foodvoter.yelpApi.models.Business;
import com.android.summer.csula.foodvoter.yelpApi.models.Coordinate;
import com.android.summer.csula.foodvoter.yelpApi.models.Location;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements RVoteAdapter.ListItemClickListener, RVoteAdapter.SwitchListener {

    private static final String EXTRA_POLL = "poll";
    private static final String POLLS_TREE = "polls";
    private static final String VOTES_TREE = "votes";
    private static final String TOAST_VOTE_RECORDED = "You're vote is recorded!";
    private static final String TOAST_VOTE_ABSENT =  "You haven't made a choice yet!";
    private static final String TOAST_WELCOME_MSG =  "Pleace select a place you like!";

    private RVoteAdapter rVoteAdapter;
    private RecyclerView rVoteRecyclerView;
    private List<Business> rChoiceData;
    private Toast mToast;
    private final static String TAG = "ListActivity";

    private Business votedBusiness;
    private DatabaseReference pollRef;      // polls/{id}
    private Poll poll;                      // the object corresponding to polls/{id}
    private DatabaseReference voteRef;      // polls/{id}/votes/{id}/  key-value <String, String>
    private String userId;


    public static Intent newIntent(Context context, Poll poll) {
        Intent intent = new Intent(context, ListActivity.class);
        intent.putExtra(EXTRA_POLL, poll.getPollId());
        return intent;
    }

    public static String getExtraPollId(Intent intent) {
        return intent.getStringExtra(EXTRA_POLL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        rVoteRecyclerView = (RecyclerView) findViewById(R.id.rv_vote_list);
        rChoiceData = new ArrayList<>();
        rVoteAdapter = new RVoteAdapter(this,rChoiceData,this,this);
        rVoteRecyclerView.setAdapter(rVoteAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rVoteRecyclerView.setLayoutManager(layoutManager);

        userId = getCurrentUserId();

        // This seems like the only way you can instantiate a toast object.
        mToast = Toast.makeText(this, TOAST_WELCOME_MSG, Toast.LENGTH_SHORT);
        mToast.show();
    }


    @Override
    protected void onResume() {
        super.onResume();

        initializeDatabaseReference();
    }

    @Override
    public void onListItemClick(Business business) {
        Intent intent = DetailActivity.newIntent(this, business);
        startActivity(intent);
    }

    /**
     * set up Firebase Database Reference to the polls node and the current vote node
     */
    private void initializeDatabaseReference() {
        String pollId = getExtraPollId(getIntent());
        pollRef = buildPollRef(pollId);
        voteRef = buildVoteRef(pollRef, userId);
        attachSingleValueListenerToPoll();
    }

    @Override
    public void onSwitchSwiped(Business business, boolean swiped) {
        String toastMessage = "";

        if(swiped){
            toastMessage = "Voted for " + business.getName();
            votedBusiness = business;
        }else{
            toastMessage = "Switched off for " + business.getName();
            votedBusiness = null;
        }

        showShortToast(toastMessage);
    }

    //For SendMyVote Button
    public void sendVote(View v){
        // Don't send vote if they user haven't voted yet!
        if (votedBusiness == null) {
            showShortToast(TOAST_VOTE_ABSENT);
            return;
        }

        // Business' id may look like business' name: "good-burger-place"
        Vote vote = new Vote(userId, votedBusiness.getId());

        // Warning, the vote is not recorded onto the Poll.class, only onto firebase json node
        writeVoteToFirebase(voteRef, vote);
        showShortToast(buildChoiceMessage(votedBusiness));
    }

    private static String buildChoiceMessage(Business business) {
        return TOAST_VOTE_RECORDED + " " + "You selected " + business.getName();
    }
    private void showShortToast(String message) {
        mToast.setText(message);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    private void writeVoteToFirebase(DatabaseReference userVoteReference, Vote vote) {
        userVoteReference.setValue(vote.getBusinessId());
    }

    /**
     * Return the id of the current logged in user.
     */
    private static String getCurrentUserId() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return firebaseUser == null ? null : firebaseUser.getUid();
    }

    /**
     * polls/{poll_id}/votes{user_id}/
     * Return Database reference of the currently logged in user voting JSON tree.
     */
    public DatabaseReference buildVoteRef(DatabaseReference pollReference, String userId) {
        return pollReference.child(VOTES_TREE).child(userId);
    }

    /**
     * Returns a DatabaseReference to polls/{pollId}.
     */
    private DatabaseReference buildPollRef(String pollId) {
        return FirebaseDatabase.getInstance()
                .getReference()
                .child(POLLS_TREE)
                .child(pollId);
    }

    /**
     * Return a value event listener that will retrieve and set a Poll.cass to  our "poll" class
     * variable.
     */
    private ValueEventListener getValueEventListenerForPoll() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                poll = dataSnapshot.getValue(Poll.class);

                if (poll != null) {
                    rVoteAdapter.swapData(poll.getBusinesses());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };
    }

    /**
     *  Attach a listener for a single event, meaning it will read perform its operation once, and
     *  stop. Thus you don't have to detach it.
     */
    private void attachSingleValueListenerToPoll() {
        ValueEventListener valueEventListener = getValueEventListenerForPoll();
        pollRef.addListenerForSingleValueEvent(valueEventListener);
    }
}

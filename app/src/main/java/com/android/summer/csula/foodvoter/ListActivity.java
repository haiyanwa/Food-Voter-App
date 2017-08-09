package com.android.summer.csula.foodvoter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.summer.csula.foodvoter.models.User;
import com.android.summer.csula.foodvoter.models.Vote;
import com.android.summer.csula.foodvoter.models.VoteRecord;
import com.android.summer.csula.foodvoter.polls.models.Poll;
import com.android.summer.csula.foodvoter.yelpApi.models.Business;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements RVoteAdapter.OnBusinessEventListener {

    private final static String TAG = "ListActivity";

    private static final String EXTRA_POLL = "poll";
    private static final String POLLS_TREE = "polls";
    private static final String VOTES_TREE = "votes";

    private RVoteAdapter rVoteAdapter;
    private RecyclerView rVoteRecyclerView;

    private DatabaseReference pollRef;                 // polls/{id}       => Poll.class
    private DatabaseReference votesRef;                // polls/{id}/votes => Map<String,String>
    private ChildEventListener votesChildEventListener;

    private Poll poll;                                 // the object corresponding to polls/{id}
    private VoteRecord voteRecord;


    public static Intent newIntent(Context context, String pollId) {
        Intent intent = new Intent(context, ListActivity.class);
        intent.putExtra(EXTRA_POLL, pollId);
        return intent;
    }

    public static String getExtraPollId(Intent intent) {
        return intent.getStringExtra(EXTRA_POLL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        voteRecord = new VoteRecord();

        createDatabaseReference();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyDatabaseReference();
    }

    private void initializedRecyclerView(boolean enableVote) {
        rVoteRecyclerView = (RecyclerView) findViewById(R.id.rv_vote_list);
        rVoteAdapter = new RVoteAdapter(this, enableVote);
        rVoteRecyclerView.setAdapter(rVoteAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rVoteRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onListItemClick(Business business) {
        Intent intent = DetailActivity.newIntent(this, business);
        startActivity(intent);
    }

    @Override
    public void onVoteCheckboxClick(String businessId, boolean checked) {
        // Warning, the vote is not recorded onto the Poll.class, only onto firebase json node
        // if businessId
        Log.d(TAG, "checkbox: " + businessId + " " + "checked: " + checked);
        if(checked) {
            writeVoteToFirebase(votesRef, getCurrentUserId(), businessId);
        } else {
            writeVoteToFirebase(votesRef, getCurrentUserId(), null);
        }
    }

    /**
     * set up Firebase Database Reference to the polls node and the current vote node
     */
    private void createDatabaseReference() {
        // Retrieve the poll class store in firebase but don't watch for any changes to the data.
        String pollId = getExtraPollId(getIntent());
        pollRef = buildPollRef(pollId);
        attachSingleValueListenerToPoll();

        // Set up a ref to watch for changes in poll/{id}/votes/
        // Every time a value is added/changed/deleted (poll/{id}/votes{id}/
        // then they respond to  it accordingly
        votesRef = buildVotesRef(pollRef);
        votesChildEventListener = getVotesChildEventListener();
        attachChildEventListener(votesRef, votesChildEventListener);
    }

    private void destroyDatabaseReference() {
        detachChildEventListener(votesRef, votesChildEventListener);
    }

    private void writeVoteToFirebase(DatabaseReference inputVoteRef, String userId, String businessId) {
        inputVoteRef
                .child(userId)
                .setValue(businessId);
    }

    /**
     * Return the id of the current logged in user.
     */
    private static String getCurrentUserId() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return firebaseUser == null ? null : firebaseUser.getUid();
    }

    /**
     * polls/{poll_id}/votes
     * Return Database reference of the currently logged in user voting JSON tree.
     */
    public DatabaseReference buildVotesRef(DatabaseReference pollReference) {
        return pollReference.child(VOTES_TREE);
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
                if (poll == null) return;   // bad data, don't precede

                boolean canVote = isCurrentUserInvited(poll.getVoters());
                displayVoteDialog(canVote);
                updateUI(canVote);

                // Store each votes recorded onto our VoteResult object
                DataSnapshot votesDataSnapshot = dataSnapshot.child("votes");
                for (DataSnapshot singleVoteSnapshot : votesDataSnapshot.getChildren()) {
                    Vote vote = new Vote(singleVoteSnapshot.getKey(), (String) singleVoteSnapshot.getValue());
                    voteRecord.recordVote(vote);
                    rVoteAdapter.addVote(vote.getBusinessId());
                }

                // Get the vote of the current user, if the user is not invited, then it will be null
                // Then update the UI checkbox to reflect their recorded vote, if any
                Vote currentUserVote = voteRecord.voteOf(getCurrentUserId());
                rVoteAdapter.recordCheckBoxVote(currentUserVote);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };
    }

    private void updateUI(boolean canVote) {
        initializedRecyclerView(canVote);
        rVoteAdapter.swapData(poll.getBusinesses());
    }

    private void displayVoteDialog(boolean canVote) {
        if (canVote) {
            displayNamesOfVoterDialog(poll.getVoters());
        } else {
            displayDeniedDialog();
        }
    }

    /**
     * Return true if the currently loged on user is one of the voters invited to the list by
     * check their id
     */
    private boolean isCurrentUserInvited(List<User> invitedVoters) {
        for (User voter : invitedVoters) {
            if (voter.getId().equals(getCurrentUserId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Attach a listener for a single event, meaning it will read perform its operation once, and
     * stop. Thus you don't have to detach it.
     */
    private void attachSingleValueListenerToPoll() {
        ValueEventListener valueEventListener = getValueEventListenerForPoll();
        pollRef.addListenerForSingleValueEvent(valueEventListener);
    }

    /**
     * Display a dialog to alert that the usr is not invited to vote in this poll.
     * They can continue viewing or leave.
     */
    private void displayDeniedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("I'm sorry you're not invited to vote in this poll, but you can still look around")
                .setTitle("Not an invited voter warning")
                .setIcon(R.drawable.ic_warning)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { }
                })
                .setNegativeButton("Leave", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        finish();
                    }
                });

        // Create the AlertDialog object and return it
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Given a list of users, return a list of each users name
     */
    private List<String> parseNamesFromList(List<User> users) {
        List<String> names = new ArrayList<>();
        for (User user : users) {
            names.add(user.getUsername());
        }
        return names;
    }

    /**
     * Display a dialog of the invited users names.
     */
    private void displayNamesOfVoterDialog(List<User> invitedVoters) {
        String names = android.text.TextUtils.join(", ", parseNamesFromList(invitedVoters));

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Voters")
                .setMessage(names)
                .setIcon(R.drawable.ic_people)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { }
                });

        // Create the AlertDialog object and return it
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * This listener will listen the key (UserId) and the value (businessId) of all votes made in:
     * {some_database_reference}/votes. Attach it to a references that contains this key/values pair
     */
    private ChildEventListener getVotesChildEventListener() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String userId = dataSnapshot.getKey();
                String businessId = (String) dataSnapshot.getValue();
                Vote vote = new Vote(userId, businessId);
                voteRecord.recordVote(vote);

                // OnChangedAdded is call once for ALL values in this tree, and then it will
                // be call whenever a child is added. When it is initially called, the adapter
                // may not be initialized because it is created inside an async function.
                if(rVoteAdapter != null) {
                    rVoteAdapter.addVote(businessId);
                    rVoteAdapter.logResults();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String userId = dataSnapshot.getKey();
                String businessId = (String) dataSnapshot.getValue();
                Vote vote = new Vote(userId, businessId);
                String oldBusinessId = voteRecord.voteOf(userId).getBusinessId();

                voteRecord.recordVote(vote);
                rVoteAdapter.swapVote(oldBusinessId, businessId);
                rVoteAdapter.logResults();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String userId = dataSnapshot.getKey();
                String businessId = (String) dataSnapshot.getValue();
                Vote vote = new Vote(userId, businessId);

                voteRecord.removeVote(vote);
                rVoteAdapter.removeVote(businessId);
                rVoteAdapter.logResults();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };
    }

    private void attachChildEventListener(DatabaseReference reference,
                                          ChildEventListener childEventListener) {
        if (childEventListener != null) {
            reference.addChildEventListener(childEventListener);
        }
    }

    private void detachChildEventListener(DatabaseReference reference,
                                          ChildEventListener childEventListener) {
        if (childEventListener != null) {
            reference.removeEventListener(childEventListener);
        }
    }
}

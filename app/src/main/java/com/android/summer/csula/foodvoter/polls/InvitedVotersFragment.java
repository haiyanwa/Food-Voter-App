package com.android.summer.csula.foodvoter.polls;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.summer.csula.foodvoter.R;
import com.android.summer.csula.foodvoter.adapters.FriendsVoterAdapter;
import com.android.summer.csula.foodvoter.database.FoodVoterFirebaseDb;
import com.android.summer.csula.foodvoter.models.User;
import com.android.summer.csula.foodvoter.polls.models.Poll;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class InvitedVotersFragment extends Fragment implements FoodVoterFirebaseDb.Listener {

    private static final String TAG = InvitedVotersFragment.class.getSimpleName();
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_VOTERS = "voters";

    private View view;

    private User author;
    private FoodVoterFirebaseDb database;
    private FriendsVoterAdapter friendsVoterAdapter;
    private List<User> voters = new ArrayList<>();
    private OnPollInvitesListener invitesListener;

    public static InvitedVotersFragment newInstance(Poll poll) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_AUTHOR, poll.getAuthor());
        // Poll#getVoters return an ArrayList; All standard implementations of
        // java.util.List already implement java.io.Serializable, so it should be safe to cast
        // https://stackoverflow.com/questions/1387954/how-to-serialize-a-list-in-java
        args.putSerializable(KEY_VOTERS, (Serializable) poll.getVoters());
        InvitedVotersFragment fragment = new InvitedVotersFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        author = (User) getArguments().getSerializable(KEY_AUTHOR);
        voters = (List<User>) getArguments().getSerializable(KEY_VOTERS);

        Log.d(TAG, "onCreate => user.toString => " + author.toString());
        Log.d(TAG, "onCreate => voters.toString => " + voters.toString());
    }


    @Override
    public void onPause() {
        super.onPause();
        database.detachReadListener();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach()");

        try {
            invitesListener = (OnPollInvitesListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, "Host Activity need to implement the interface: "
                    + OnPollInvitesListener.class.toString(), e);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_invites, container, false);

        initializeUI();

        initializeDatabase();

        return view;
    }

    private void initializeUI() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        friendsVoterAdapter = new FriendsVoterAdapter(invitesListener);
        RecyclerView friendsRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_friends_list);
        friendsRecyclerView.setLayoutManager(layoutManager);
        friendsRecyclerView.setAdapter(friendsVoterAdapter);
    }

    private void initializeDatabase() {
        database = new FoodVoterFirebaseDb(this, author.getId());
        database.attachReadListener();
    }

    @Override
    public void onUserAdded(User user) { }

    @Override
    public void onUserChanged(User user) { }


    // Firebase database will call this method whenever a friend ha been added to the
    // users friendship node.
    @Override
    public void onFriendAdded(User user) {
        Log.d(TAG, "friends added: " + user.toString());

        friendsVoterAdapter.addFriend(user);

        // Check the voter list, passed to by the activity,  to see if the "user"
        // has already been invited. If so, update its visual through the adapter
        for (User voter: voters) {
            if (voter.equals(user)) {
                friendsVoterAdapter.updateInvitedUser(user);
                return;
            }
        }

    }

    /**
     * This interface is used to inform the host activity that a user have been invited/univted to
     * a poll
     */
    public interface OnPollInvitesListener {

        void onUserInvited(User voter, boolean invited);
    }
}

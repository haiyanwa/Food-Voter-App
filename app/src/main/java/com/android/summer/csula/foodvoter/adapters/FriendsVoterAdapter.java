package com.android.summer.csula.foodvoter.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.summer.csula.foodvoter.R;
import com.android.summer.csula.foodvoter.models.Invitee;
import com.android.summer.csula.foodvoter.models.User;
import com.android.summer.csula.foodvoter.polls.InvitedVotersFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * This adapter is used to add a User's friend as voter.
 */
public class FriendsVoterAdapter extends RecyclerView.Adapter<FriendsVoterAdapter.ViewHolder> {

    private static final String TAG = FriendsVoterAdapter.class.getSimpleName();


    private List<Invitee> friends = new ArrayList<>();

    private InvitedVotersFragment.OnPollInvitesListener listener;

    public FriendsVoterAdapter(InvitedVotersFragment.OnPollInvitesListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attachToParent = false;
        int itemFriendsInvitesResId = R.layout.item_friends_invites_list;

        View view = inflater.inflate(itemFriendsInvitesResId, parent, attachToParent);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public void updateInvitedUser(User user) {
        for (Invitee inv : friends) {
            if (inv.getUser().getId().equals(user.getId())) {
                inv.setInvited(true);
                notifyDataSetChanged();
                return;
            }
        }
    }

    public void addFriend(User user) {
        friends.add(new Invitee(user));
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView usernameText;
        private ImageView presenceImage;
        private CheckBox inviteCheckbox;

        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            usernameText = (TextView) view.findViewById(R.id.tv_friend_username);
            presenceImage = (ImageView) view.findViewById(R.id.iv_friend_presence);
            inviteCheckbox = (CheckBox) view.findViewById(R.id.checkbox_invite_voter);


            inviteCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    int position = getAdapterPosition();
                    Invitee invitee = friends.get(position);

                    // TODO: once we implement notification we can invited offline user
                    // but for now we will not allow it
                    if (!invitee.getUser().isOnline()) {
                        Toast.makeText(view.getContext(), "Can't invite User who are not log on!", Toast.LENGTH_LONG).show();
                        inviteCheckbox.setChecked(false);
                        return;
                    }

                    // Invite/Uninvite the user
                    inviteCheckbox.setChecked(isChecked);
                    invitee.setInvited(isChecked);
                    listener.onUserInvited(invitee.getUser(), isChecked);
                }
            });
        }

        public void bind(int position) {
            Invitee invitee = friends.get(position);
            User friend = invitee.getUser();

            usernameText.setText(friend.getUsername());
            inviteCheckbox.setChecked(invitee.isInvited());

            if (friend.isOnline()) {
                presenceImage.setImageResource(android.R.drawable.presence_online);
            } else {
                presenceImage.setImageResource(android.R.drawable.presence_offline);
            }
        }
    }
}

package com.android.summer.csula.foodvoter.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.summer.csula.foodvoter.R;
import com.android.summer.csula.foodvoter.models.User;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private static final String TAG = UsersAdapter.class.getSimpleName();

    private List<User> users = new ArrayList<>();
    private UserAdapterListener listener;
    private  int imageResId;

    public UsersAdapter(UserAdapterListener listener, int imageResId) {
        this.listener = listener;
        this.imageResId = imageResId;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attachToParent = false;
        int friendItemResId = R.layout.item_users_list;

        View view = inflater.inflate(friendItemResId, parent, attachToParent);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(holder, position);
    }

    @Override
    public int getItemCount() {
        return users == null ? 0 : users.size();
    }

    public void clear() {
        users.clear();
        notifyDataSetChanged();
    }

    public void add(User friend) {
        users.add(friend);
        notifyDataSetChanged();
    }

    /* Check if the input user is a "friend" and it is updateOnlineStatus its online status */
    public void updateOnlineStatus(User updatedUser) {
        for (User friend : users) {
            if (friend.equals(updatedUser)) {
                friend.setOnline(updatedUser.isOnline());
                notifyDataSetChanged();
                break;
            }
        }
    }

    public void remove(int position) {
        users.remove(position);
        notifyDataSetChanged();
    }

    public void remove(User user) {
        boolean removalResult = users.remove(user);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView username;
        private ImageView presence;
        private ImageButton actionButton;

        public ViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.tv_friend_username);
            presence = (ImageView) itemView.findViewById(R.id.iv_friend_presence);
            actionButton = (ImageButton) itemView.findViewById(R.id.btn_friend_action);
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = getAdapterPosition();
                    User user = users.get(index);

                    listener.onUserClick(user);
                }
            });
        }

        public void bind(ViewHolder holder, int position) {
            User user = users.get(position);
            username.setText(user.getUsername());

            if (user.isOnline()) {
                presence.setImageResource(android.R.drawable.presence_online);
            } else {
                presence.setImageResource(android.R.drawable.presence_offline);
            }

            holder.itemView.setTag(user);
            actionButton.setImageResource(imageResId);

        }
    }

    public interface UserAdapterListener {
        void onUserClick(User user);
    }
}

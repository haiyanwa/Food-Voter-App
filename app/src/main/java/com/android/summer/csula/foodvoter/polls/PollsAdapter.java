package com.android.summer.csula.foodvoter.polls;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.summer.csula.foodvoter.R;
import com.android.summer.csula.foodvoter.polls.models.Poll;

import java.util.ArrayList;
import java.util.List;

public class PollsAdapter extends RecyclerView.Adapter<PollsAdapter.PollViewHolder> {

    private static final String TAG = PollActivity.class.getSimpleName();
    private List<Poll> polls = new ArrayList<>();
    private OnPollClickListener onPollClickListener;

    /**
     * Show a different icon, if it is true
     * TODO: replace this with resID, less confusing
     */
    private boolean invited;

    public PollsAdapter(OnPollClickListener onPollClickListener, boolean invited) {
        this.onPollClickListener = onPollClickListener;
        this.invited = invited;
    }

    @Override
    public PollViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        int pollItemListResId = R.layout.item_poll_list;

        View view = layoutInflater.inflate(pollItemListResId, parent, false);

        return new PollViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PollViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return polls.size();
    }

    public void addPoll(Poll poll) {
        polls.add(0, poll);  // add the item to head of the list
        notifyDataSetChanged();
    }


    public class PollViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView completed;
        private TextView description;
        private ImageView image;

        public PollViewHolder(View itemView) {
            super(itemView);

            initializeView(itemView);

            initializeViewClickListener(itemView);
        }

        private void initializeView(View view) {
            title = (TextView) view.findViewById(R.id.textview_poll_title);
            completed = (TextView) view.findViewById(R.id.textview_completed);
            description = (TextView) view.findViewById(R.id.textview_description);
            image = (ImageView) view.findViewById(R.id.imageview_poll);
        }


        private void initializeViewClickListener(View itemView) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Poll poll = polls.get(position);
                    onPollClickListener.onPollClick(poll);
                }
            });
        }

        public void bind(int position) {
            Poll current = polls.get(position);
            Log.d(TAG, "current bind: " + current.toString());

            title.setText(current.getTitle());
            description.setText(current.getDescription());
            completed.setText(current.isCompleted() ? "Completed" : "Not Completed");

            if (invited) {
                image.setImageResource(R.drawable.ic_restaurant);
            }
        }
    }


    public interface OnPollClickListener {
        void onPollClick(Poll poll);
    }
}

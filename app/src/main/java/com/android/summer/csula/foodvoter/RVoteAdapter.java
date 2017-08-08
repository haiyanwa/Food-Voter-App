package com.android.summer.csula.foodvoter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import com.android.summer.csula.foodvoter.models.BusinessVoteHelper;
import com.android.summer.csula.foodvoter.yelpApi.models.Business;
import com.android.summer.csula.foodvoter.yelpApi.models.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Haiyan on 7/19/17.
 */

public class RVoteAdapter extends RecyclerView.Adapter<RVoteAdapter.ViewHolder>{

    final private ListItemClickListener mOnClickListener;

    private SwitchListener switchListener;

    private static int viewHolderCount;

    private int mNumberItems;

    private final Context mContext;

    private List<BusinessVoteHelper> mChoiceData;

    private final int ListItem = 0;
    private final int EndOfList= 1;

    private String TAG = "RVoteAdapter";

    public interface ListItemClickListener{
        void onListItemClick(Business business);
    }

    public interface SwitchListener{
        //void onSwitchSwiped(int swipedItemIndex, boolean swiped);
        void onSwitchSwiped(Business swipedItem, boolean swiped);
    }


    /**public RVoteAdapter(List<restaurant> restaurants, ListItemClickListener listener, SwitchListener swListener) {
        mChoiceData = restaurants;
        mOnClickListener = listener;
        switchListener = swListener;

    }*/
    public RVoteAdapter(@NonNull Context context, List<Business> businesses, ListItemClickListener listener, SwitchListener swListener) {
        mContext = context;
        mChoiceData = wrapBusiness(businesses);
        mOnClickListener = listener;
        switchListener = swListener;
    }

    /**
     * Wrap every bussiness object into a BusinesVoteHelper class so we can store data for it is
     * is selected or not.
     */
    private static List<BusinessVoteHelper> wrapBusiness(List<Business> businesses) {
        List<BusinessVoteHelper> businessVoteHelpers = new ArrayList<>();
        for (Business business : businesses) {
            businessVoteHelpers.add(new BusinessVoteHelper(business));
        }
        return businessVoteHelpers;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mChoiceData.size()) ? EndOfList : ListItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if(viewType == EndOfList){
            view = inflater.inflate(R.layout.vote_submit_btn, parent, false);
        }else{
            view = inflater.inflate(R.layout.list_item, parent, false);
        }
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(holder, position);
    }

    @Override
    public int getItemCount() {
        if (null == mChoiceData) return 0;
        return mChoiceData.size() + 1;
    }

    public void swapData(List<Business> businesses) {
        //replace the old data with new data and force the recyclerView to refresh
        this.mChoiceData = wrapBusiness(businesses);
        this.notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView choiceItemView;
        public ImageView choiceImageView;
        public TextView choiceDescView;
        public RatingBar choiceRatingView;
        public Switch voteSwitch;
        public Button voteButton;
        int index;

        public ViewHolder(View view) {
            super(view);
            choiceItemView = (TextView) view.findViewById(R.id.rv_choice_item_title);
            choiceImageView = (ImageView) view.findViewById(R.id.rv_choice_item_image);
            choiceDescView = (TextView) view.findViewById(R.id.rv_choice_item_desc);
            choiceRatingView = (RatingBar) view.findViewById(R.id.rv_choice_ratingBar);
            voteSwitch = (Switch) view.findViewById(R.id.rv_vote_switch);
            voteButton = (Button) view.findViewById(R.id.rv_vote_btn);
            view.setOnClickListener(this);
        }

        public void bind(ViewHolder holder, int position){
            if(position < mChoiceData.size()){
                //restaurant restaurant = mChoiceData.get(position);
                final Business business = mChoiceData.get(position).getBusiness();
                choiceItemView.setText(business.getName());
                List<Category> categories = business.getCategories();
                String list = "";
                for(Category category : categories){
                   list = list + " " + category.getTitle();
                }
                choiceDescView.setText(list);
                choiceRatingView.setRating((float)business.getRating());

                String imageUri = business.getImageUrl();
                Picasso.with(mContext).load(imageUri).fit().centerCrop()
                        .placeholder(R.drawable.restaurant_default_image)
                        .into(choiceImageView);

                voteSwitch.setTextOn("Yes");
                voteSwitch.setTextOff("No");

                // Switch are checked base on its model (BusinessVoteHelper)
                index = position;
                BusinessVoteHelper voteHelper = mChoiceData.get(position);
                voteSwitch.setChecked(voteHelper.isSelected());

                holder.voteSwitch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BusinessVoteHelper voteHelper = mChoiceData.get(getAdapterPosition());

                        // switch a unchecked switch to on, and turn of all other switches
                        if(!voteHelper.isSelected()) {
                            deselectAll();
                            notifyDataSetChanged();
                        }

                        // update the model
                        voteHelper.setSelected(!voteHelper.isSelected());

                        switchListener.onSwitchSwiped(voteHelper.getBusiness(), voteHelper.isSelected());
                    }
                });
            }
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Business business = mChoiceData.get(pos).getBusiness();
            mOnClickListener.onListItemClick(business);
        }

        /**
         * Set BusinessVoteHelper.isSelected to false for all values in mChoiceData
         */
        private void deselectAll() {
            for (BusinessVoteHelper businessVoteHelper : mChoiceData) {
                businessVoteHelper.setSelected(false);
            }
        }
    }
}


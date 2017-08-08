package com.android.summer.csula.foodvoter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
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
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(holder, position);
    }

    @Override
    public int getItemCount() {
        return mChoiceData == null ? 0 : mChoiceData.size();
    }

    public void swapData(List<Business> businesses) {
        //replace the old data with new data and force the recyclerView to refresh
        this.mChoiceData = wrapBusiness(businesses);
        this.notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

        public TextView choiceItemView;
        public ImageView choiceImageView;
        public TextView choiceDescView;
        public RatingBar choiceRatingView;
        public CheckBox voteCheckbox;

        public ViewHolder(View view) {
            super(view);
            choiceItemView = (TextView) view.findViewById(R.id.rv_choice_item_title);
            choiceImageView = (ImageView) view.findViewById(R.id.rv_choice_item_image);
            choiceDescView = (TextView) view.findViewById(R.id.rv_choice_item_desc);
            choiceRatingView = (RatingBar) view.findViewById(R.id.rv_choice_ratingBar);
            voteCheckbox = (CheckBox) view.findViewById(R.id.rv_vote_checkbox);

            // Use long click over normal because people keep accidently clicking on the view
            // when they trying to click on the checkbox
            view.setOnLongClickListener(this);
            choiceImageView.setOnClickListener(this);
            voteCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BusinessVoteHelper voteHelper = mChoiceData.get(getAdapterPosition());

                    // Allow only for one checkbox to be checked
                    if (!voteHelper.isSelected()) {
                        deselectAll();
                        notifyDataSetChanged();
                    }

                    // update the model
                    voteHelper.setSelected(!voteHelper.isSelected());

                    switchListener.onSwitchSwiped(voteHelper.getBusiness(), voteHelper.isSelected());
                }
            });
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


                // Switch are checked base on its model (BusinessVoteHelper)
                BusinessVoteHelper voteHelper = mChoiceData.get(position);
                voteCheckbox.setChecked(voteHelper.isSelected());
            }
        }

        /**
         * Set BusinessVoteHelper.isSelected to false for all values in mChoiceData
         */
        private void deselectAll() {
            for (BusinessVoteHelper businessVoteHelper : mChoiceData) {
                businessVoteHelper.setSelected(false);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            int pos = getAdapterPosition();
            Business business = mChoiceData.get(pos).getBusiness();
            mOnClickListener.onListItemClick(business);
            return true;
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            Business business = mChoiceData.get(pos).getBusiness();
            mOnClickListener.onListItemClick(business);
        }
    }
}


package com.android.summer.csula.foodvoter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.summer.csula.foodvoter.models.VoteResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Haiyan on 8/8/17.
 */

public class ResultAdapter extends ArrayAdapter<VoteResult> {

    private Context mContext;
    private List<VoteResult> mVoteResults;
    private final String TAG = "ResultAdapter";

    public ResultAdapter(Context context, int resourceId, List<VoteResult> voteResults) {
        super(context, resourceId, voteResults);
        this.mContext = context;
        this.mVoteResults = voteResults;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.table_row, parent, false);
        }
        TextView businessNameView = (TextView)convertView.findViewById(R.id.business_name);
        TextView voteCountView = (TextView) convertView.findViewById(R.id.vote_count);

        VoteResult vResult = getItem(position);

        businessNameView.setText(vResult.getBusinessName());
        voteCountView.setText(vResult.getCount().toString());

        return convertView;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    public void swapData(List<VoteResult> voteResults) {
        //replace the old data with new data and force the recyclerView to refresh
        ArrayList<VoteResult> results = new ArrayList<>();
        for(VoteResult voteResult : voteResults){
            Log.d(TAG, "voteResult " + voteResult.getBusinessName());
            results.add(voteResult);
        }
        this.mVoteResults = results;
        this.notifyDataSetChanged();
    }
}

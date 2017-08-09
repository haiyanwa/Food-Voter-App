package com.android.summer.csula.foodvoter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.android.summer.csula.foodvoter.models.Vote;
import com.android.summer.csula.foodvoter.models.VoteResult;
import com.android.summer.csula.foodvoter.polls.models.Poll;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.android.summer.csula.foodvoter.ListActivity.getExtraPollId;

public class TableResultActivity extends AppCompatActivity {

    private final String TAG = "TableResultActivity";
    private static final String POLLS_TREE = "polls";
    private static final String VOTES_TREE = "votes";

    private ListView listview;
    private ResultAdapter resultAdapter;
    private DatabaseReference mVoteReference;
    private Poll poll;
    private ChildEventListener mChildEventListener;
    private List<VoteResult> mUserVoteResults;
    private Map<String, String> mUserVotes = new TreeMap<>();   //Map<UserId, BusinessName>
    private Map<String, Integer> voteCounts = new TreeMap<>();  //Map<BusinessName, counts>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        listview = (ListView) findViewById(R.id.result_list_view);

        //String pollId ="-KqvOTVDiHXeHPdEJKrq";
        String pollId = getExtraPollId(getIntent());
        mVoteReference = getPollResultRef(pollId);
        if(mVoteReference == null){
            Log.d(TAG, "mVoteReference is null");
        }
        //for test, will be replaced by data from db
        /**Map<String, Integer> voteCounts = new TreeMap<>();
        voteCounts.put("Taco King", 3);
        voteCounts.put("Northen cafe", 4);
        voteCounts.put("Panda Experss", 1);*/

        mUserVoteResults = new ArrayList<>();
        /**for (String key : voteCounts.keySet()) {
            VoteResult vote_result = new VoteResult(key, voteCounts.get(key));
            resultList.add(vote_result);
        }*/
        resultAdapter = new ResultAdapter(this, R.layout.table_row, mUserVoteResults);
        Log.e(TAG, "mVoteReference" + mVoteReference.getKey());
        listview.setAdapter(resultAdapter);

        //attachDatabaseReadListener();
        mVoteReference.addValueEventListener(getValueEventListenerForVote());
    }

    //get data under /polls/{id}/votes/
    private DatabaseReference getPollResultRef(String pollId) {
        return FirebaseDatabase.getInstance()
                .getReference()
                .child(POLLS_TREE)
                .child(pollId)
                .child(VOTES_TREE);
    }

    private ValueEventListener getValueEventListenerForVote() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String userid = snapshot.getKey().toString();
                    String businessName = snapshot.getValue().toString();
                    Vote vote = new Vote(userid, businessName);
                    mUserVotes.put(vote.getUserId(), vote.getBusinessName());
                 }
                countVotes();
                ArrayList<VoteResult> resultList = convertToList(voteCounts);
                for(int i=0;i<resultList.size();i++){
                    Log.d(TAG, "Vote " + resultList.get(i).getBusinessName() + " " + resultList.get(i).getCount());
                }
                resultAdapter.clear();
                resultAdapter.addAll(resultList);
                resultAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };
    }

    //count votes and update Map voteCounts
    private void countVotes(){
        for(String userid : mUserVotes.keySet()){
            String businessName = mUserVotes.get(userid);
            if(voteCounts == null){
                voteCounts.put(businessName, 1);
            }else{
                if(voteCounts.get(businessName) != null){
                    int count = voteCounts.get(businessName);
                    count++;
                    voteCounts.put(businessName, count);
                }else{
                    voteCounts.put(businessName,1);
                }
            }
        }
    }

    private ArrayList<VoteResult> convertToList(Map<String, Integer> counts){
        ArrayList<VoteResult> resultList = new ArrayList<>();
        for (String key : counts.keySet()) {
            VoteResult vote_result = new VoteResult(key, counts.get(key));
            resultList.add(vote_result);
        }
        return resultList;
    }

}

package com.android.summer.csula.foodvoter.polls;


import com.android.summer.csula.foodvoter.polls.models.Poll;
import com.android.summer.csula.foodvoter.yelpApi.models.Coordinate;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URL;

import static com.android.summer.csula.foodvoter.yelpApi.tasks.RequestYelpSearchTask.SearchBuilder;

public class PollUtilities {

    private static final DatabaseReference POLLS_REFERENCE = setupPollsReference();

    /**
     * Build the Yelp Search URL based on the input Poll object.
     */
    public static URL toURL(Poll poll) throws Exception {
        Coordinate coordinate = poll.getCoordinate();
        String zipCode = poll.getZipCode();

        SearchBuilder builder = new SearchBuilder()
                .openNow(poll.isOpenNow())
                .price(poll.getPrice());

        // We either use coordinate or zip-code but not both, prioritize coordinate
        if (coordinate != null) {
            builder
                    .latitude(String.valueOf(coordinate.getLatitude()))
                    .longitude(String.valueOf(coordinate.getLongitude()));
        } else {
            builder.location(zipCode);
        }
        return builder.build();
    }


    /**
     * Write the Poll object onto firebase.
     */
    public static void writeToFirebase(Poll poll) {
        DatabaseReference currentPollReference = POLLS_REFERENCE.push();

        // Use the database id generate by firebase as the poll_id.
        poll.setPollId(currentPollReference.getKey());
        poll.setActiveOn(System.currentTimeMillis());
        currentPollReference.setValue(poll);
    }

    /**
     * Return a Firebase Database Reference pointing to the "polls" JSON tree
     */
    private static DatabaseReference setupPollsReference() {
        return FirebaseDatabase.getInstance()
                .getReference()
                .child("polls");
    }
}

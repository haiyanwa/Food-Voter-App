const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);


// Keep TTL short for demonstration purposes
const TIME_TO_LIVE = 60; // seconds

// Send a Heads-Up Notification to all the invited voters whenever
// a poll is created
exports.messageVoters = functions.database.ref("/polls/{id}/voters")
    .onCreate(event => {
        const parentRef = event.data.ref.parent;

        return parentRef.once('value')
        // gather Poll data so it could be user later
            .then(dataSnapshot => {
                return {
                    username: dataSnapshot.val().author.username,
                    title: dataSnapshot.val().title,
                    description: dataSnapshot.val().description,
                    timeLimitInMs: dataSnapshot.val().timeLimitInMs
                }
            })
            // use the poll info to create a payload and send it to all the voters
            .then(poll => {
                const votersRef = event.data.ref;

                const payload = {
                    data: {
                        title: `You're in invited by ${poll.username} to vote in Food Poll!`,
                        body: `${poll.title} -  ${poll.description}. Good Luck Friend!`
                    }
                };

                const options = {
                    priority: "high",
                    timeToLive: TIME_TO_LIVE      //  in seconds
                };

                // obtain the token for each voter and send them the payload
                votersRef.once('value')
                    .then(dataSnapshot => {
                        dataSnapshot.forEach(child => {
                            const voter = child.val();
                            admin.messaging().sendToDevice(voter.token, payload, options);
                        });
                    });

            })
    });

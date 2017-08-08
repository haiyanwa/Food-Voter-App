package com.android.summer.csula.foodvoter.data;

/**
 * Created by Owner on 8/5/2017.
 */

public class RestaurantTestData_TableResults {

        private int id;
        private String name;
        private String vote;
        private static final String[] rest = {"Popeyes", "KFC", "Taco Bell"};
        private static final String[] votes = { "5", " 3", "1"};


        public RestaurantTestData_TableResults(int id, String name, String vote) {
            this.id = id;
            this.name = name;
            this.vote = vote;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String isVote() {
            return vote;
        }

        public void setVote(String vote) {
            this.vote = vote;
        }


        public static String getRest() {
            String rest0 = rest[0];
            return rest0;
        }

        public static String getRest1() {
            String rest1 = rest[1];
            return rest1;
        }

        public static String getRest2() {
            String rest2 = rest[2];
            return rest2;
        }


        public static String getVote() {
            String votes0 = votes[0];
            return votes0;
        }

        public static String getVote1() {
            String votes1 = votes[1];
            return votes1;
        }

        public static String getVote2() {
            String votes2 = votes[2];
            return votes2;
        }


    }


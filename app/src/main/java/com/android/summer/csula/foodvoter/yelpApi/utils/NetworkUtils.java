package com.android.summer.csula.foodvoter.yelpApi.utils;


import android.support.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    public static final String POST_REQUEST = "POST";
    public static final String GET_REQUEST = "GET";

    public static String getJsonResponseFromHttpUrl(URL url,
                                                    String requestMethod,
                                                    @Nullable String authorization) throws IOException {


        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod(requestMethod);
        urlConnection.setRequestProperty("Content-Type", "application/json");

        if (authorization != null) {
            urlConnection.setRequestProperty("authorization", authorization);
        }

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}

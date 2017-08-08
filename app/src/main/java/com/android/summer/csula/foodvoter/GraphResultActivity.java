package com.android.summer.csula.foodvoter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class GraphResultActivity extends AppCompatActivity {

    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_result);

        barChart = (BarChart) findViewById(R.id.bargraph);

        // Y Axis Data
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(44f, 0));
        barEntries.add(new BarEntry(88f, 1));
        barEntries.add(new BarEntry(66f, 2));
        barEntries.add(new BarEntry(12f, 3));
        barEntries.add(new BarEntry(44f, 4));
        BarDataSet barDataSet = new BarDataSet(barEntries, "Restaurants");

        // X Axis Data
        ArrayList<String> theRestaurants = new ArrayList<>();
        theRestaurants.add("ChikFilA");
        theRestaurants.add("Blaze");
        theRestaurants.add("In N Out");
        theRestaurants.add("TGI Friday");
        theRestaurants.add("Domino's");

        BarData theData = new BarData(theRestaurants, barDataSet);
        barChart.setData(theData);

        barChart.setTouchEnabled(true);

    }
}

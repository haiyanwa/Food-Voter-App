package com.android.summer.csula.foodvoter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private Button homeBtn;
    private Button kailaBtn;
    private Button samanBtn;

    private Intent launchActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeBtn= (Button) findViewById(R.id.home_btn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivityIntent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(launchActivityIntent);
            }
        });

        kailaBtn = (Button) findViewById(R.id.kaila_btn);
        kailaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivityIntent = new Intent(MainActivity.this, TableResultActivity.class);
                startActivity(launchActivityIntent);
            }
        });

        samanBtn = (Button) findViewById(R.id.saman_btn);
        samanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivityIntent = new Intent(MainActivity.this, GraphResultActivity.class);
                startActivity(launchActivityIntent);
            }
        });
    }
}

package com.android.summer.csula.foodvoter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.summer.csula.foodvoter.models.Details;
import com.android.summer.csula.foodvoter.yelpApi.models.Business;
import com.android.summer.csula.foodvoter.yelpApi.models.Coordinate;
import com.squareup.picasso.Picasso;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.util.List;


public class DetailActivity extends AppCompatActivity {
    private static final String EXTRA_BUSINESS = "business";
    public TextView mName, mPhone, mAddress, mUrl, mPrice;
    public ImageView mImageURL;
    public RatingBar mRating;
    public CheckBox mCheckBox$, mCheckBox$$, mCheckBox$$$, mCheckBox$$$$;
    public String price;

    Business mBusiness;


    //this is tempoary, will need to find a way to get data for long and lat
    String MAP_API_ENDPOINT;

//    public String STATIC_MAP_API_ENDPOINT ="http://maps.google.com/maps/api/staticmap?center="+longitude+","+lattitude+"&zoom=15&size=2000x500&scale=2&sensor=false";
//    static final String STATIC_MAP_API_ENDPOINT = "http://maps.google.com/maps/api/staticmap?center=34.004507,-118.256703&zoom=13&markers=size:mid|color:red|label:E|34.004507,-118.256703&size=1500x300&sensor=false";


    public static Intent newIntent(Context context, Business business) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_BUSINESS, business);
        return intent;
    }


//    Intent intent = getIntent();
//    Bundle bundle = intent.getExtras();
//    String filter = bundle.getString("filter");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

//        mPrice = (TextView) findViewById(R.id.price);
        mName = (TextView) findViewById(R.id.restaurantName);
        mPhone = (TextView) findViewById(R.id.phoneNumber);
        mUrl = (TextView) findViewById(R.id.url);
        mAddress = (TextView) findViewById(R.id.address);
        mRating = (RatingBar) findViewById(R.id.ratingsBar);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null){
            mBusiness = (Business) getIntent().getSerializableExtra(EXTRA_BUSINESS);

            mName.setText(mBusiness.getName());
            mPhone.setText(mBusiness.getDisplayPhone());
            mAddress.setText(parseAddressArray(mBusiness.getLocation().getDisplayAddress()));
            mRating.setRating((float) mBusiness.getRating());

            CheckboxChecking(mBusiness.getPrice());

            mUrl.setText(mBusiness.getUrl());
            Linkify.addLinks(mUrl, Linkify.WEB_URLS);

            ImageView mImgURL = (ImageView) findViewById(R.id.imgURL);
            Picasso.with(this)
                    .load(mBusiness.getImageUrl())
                    .into(mImgURL);


            Coordinate coordinate = mBusiness.getCoordinate();
            MAP_API_ENDPOINT ="http://maps.google.com/maps/api/staticmap?center="+coordinate.getLatitude()+","+coordinate.getLongitude()+"&zoom=15&size=2000x500&scale=2&sensor=false";

            //setting Rest Name for collapsingToolbar
            CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            collapsingToolbar.setTitle(mBusiness.getName());
            collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
            collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        }

        if (bundle == null){
                    viewModel();
        }



        AsyncTask<Void, Void, Bitmap> setImageFromUrl = new AsyncTask<Void, Void, Bitmap>(){
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap bmp = null;
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet request = new HttpGet(MAP_API_ENDPOINT);

                InputStream in = null;
                try {
                    in = httpclient.execute(request).getEntity().getContent();
                    bmp = BitmapFactory.decodeStream(in);
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return bmp;
            }
            protected void onPostExecute(Bitmap bmp) {
                if (bmp!=null) {
                    final ImageView iv = (ImageView) findViewById(R.id.img);
                    iv.setImageBitmap(bmp);
                }
            }
        };
        setImageFromUrl.execute();

    }

    public void CheckboxChecking(String price){
        mCheckBox$ = (CheckBox) findViewById(R.id.$);
        mCheckBox$$ = (CheckBox) findViewById(R.id.$$);
        mCheckBox$$$ = (CheckBox) findViewById(R.id.$$$);
        mCheckBox$$$$ = (CheckBox) findViewById(R.id.$$$$);

        if (price.equals("$$$$")){
            mCheckBox$$$$.setChecked(true);;
        }
        else if (price.equals("$$$")){
            mCheckBox$$$.setChecked(true);
        }
        else if (price.equals("$$")){
            mCheckBox$$.setChecked(true);
        }
        else{
            mCheckBox$.setChecked(true);
        }
    }


//method for testing view
    public void viewModel(){
        Details details = new Details();
        String longitude = "34.137022";
        String latitude = "-118.352266";

        String phoneNumber = "(818) 753-4867";
        String restAddress = "1000 Universal Studios Blvd #114, Universal City, CA 91608";
        String restName = "Bubba Gump Shrimp Co.";
        mUrl = (TextView) findViewById(R.id.url);
        mUrl.setText("http://www.yelp.com/biz/yelp-san-francisco");
        Linkify.addLinks(mUrl, Linkify.WEB_URLS);


        mPhone = (TextView) findViewById(R.id.phoneNumber);
        details.setPhoneNumber(phoneNumber);
        mPhone.setText(details.getPhoneNumber());

        mAddress = (TextView) findViewById(R.id.address);
        details.setAddress("140 New Montgomery St Financial District San Francisco, CA 94105");
        mAddress.setText(details.getAddress());


        mName = (TextView) findViewById(R.id.restaurantName);
        details.setRestaurantName(restName);
        mName.setText(details.getRestaurantName());
        mName.setVisibility(View.INVISIBLE);

        mImageURL = (ImageView) findViewById(R.id.imgURL);
        details.setImgURL("http://s3-media3.fl.yelpcdn.com/bphoto/nQK-6_vZMt5n88zsAS94ew/o.jpg");
        Picasso.with(this)
                .load(details.getImgURL())
                .into(mImageURL);

         MAP_API_ENDPOINT ="http://maps.google.com/maps/api/staticmap?center="+longitude+","+latitude+"&zoom=15&size=2000x500&scale=2&sensor=false";

        //setting Rest Name
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(details.getRestaurantName());

        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
    }



    private static String parseAddressArray(List<String> displayAddress){
        String address = "";
        for (String str : displayAddress) {
            address += str + "\n";
        }
        return address;
    }
}

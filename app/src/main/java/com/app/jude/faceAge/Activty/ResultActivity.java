package com.app.jude.faceAge.Activty;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.jude.faceAge.Ads.Admob;
import com.app.jude.faceAge.Ads.AudienceNetworkAds;
import com.app.jude.faceAge.Ads.GoogleAnalyticsApplication;
import com.app.jude.faceAge.CustomAdapter;
import com.app.jude.faceAge.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.microsoft.projectoxford.face.contract.Face;

import static com.app.jude.faceAge.Ads.Admob.adsLayout1;
import static com.app.jude.faceAge.Ads.AudienceNetworkAds.adView;


public class ResultActivity extends AppCompatActivity {
    String data;
    byte[] byteArray;
    private Tracker mTracker;
    public View view;
    private static AdView mAdView;
    public static LinearLayout adsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

         data = getIntent().getStringExtra("list_faces");
         AudienceNetworkAds.facebookInterstitialAd(this);
      //   AudienceNetworkAds.facebookLoadBanner(getApplicationContext(), view);

        GoogleAnalyticsApplication application = (GoogleAnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        view=getWindow().getDecorView().getRootView();


        //Admob.createLoadInterstitial(getApplicationContext(),null);
        AudienceNetworkAds.facebookLoadBanner(getApplicationContext(), view);

        Gson gson = new Gson();
        Face[] faces = gson.fromJson(data, Face[].class);

        ListView myListView = findViewById(R.id.listView);

        byteArray = getIntent().getByteArrayExtra("image");


        Bitmap orig = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        if (faces == null) {
            if (data == null) {
                Toast.makeText(getApplicationContext(), getString(R.string.array_null), Toast.LENGTH_LONG).show();
            } else {

            }
        } else {
            try {
                CustomAdapter customAdapter = new CustomAdapter(faces, this, orig);
                myListView.setAdapter(customAdapter);
            } catch (Exception e) {
                makeToast(e.getMessage());
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

        mTracker.setScreenName("Sonuç Ekranı");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }
    private void makeToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }



}

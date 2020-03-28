package com.app.jude.faceAge.Ads;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.jude.faceAge.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;



public class AudienceNetworkAds {


    public static AdView adView;
    public static InterstitialAd interstitialAd;
    public static LinearLayout adViewContainer;
    public static Context cnt;




    public static void facebookLoadBanner(final Context context, final View rootView) {

        adViewContainer = (LinearLayout) rootView.findViewById(R.id.adViewContainer);

        adView = new AdView(context, context.getResources().getString(R.string.facebook_banner)
                , AdSize.BANNER_HEIGHT_50);
        adViewContainer.addView(adView);
        adView.loadAd();
        cnt=context;



        adView.setAdListener(new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {

                Log.i("FacebookAD","info: "+ adError);
               Admob.createLoadBanner(context, rootView);


            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });




        AdSettings.addTestDevice("6f0f17dd-b085-4a10-a9e3-b1103c6752a9");


    }





    public static void facebookInterstitialAd(final Activity context)
    {


        interstitialAd = new InterstitialAd(context, context.getResources().getString(
                R.string.facebook_Intersitial));

        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {


            }


            @Override
            public void onInterstitialDismissed(Ad ad) {


            }

            @Override
            public void onError(Ad ad, AdError adError) {

                Admob.createLoadInterstitial(context);
            }

            @Override
            public void onAdLoaded(Ad ad) {

                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {


            }

            @Override
            public void onLoggingImpression(Ad ad) {



            }
        });
        interstitialAd.loadAd();

        cnt=context;


        AdSettings.setIntegrationErrorMode(AdSettings.IntegrationErrorMode.INTEGRATION_ERROR_CALLBACK_MODE);

        AdSettings.addTestDevice("6f0f17dd-b085-4a10-a9e3-b1103c6752a9");

    }




}

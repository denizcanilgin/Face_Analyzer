package com.app.jude.faceAge.Activty;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


import com.app.jude.faceAge.Ads.AudienceNetworkAds;
import com.app.jude.faceAge.CustomAdapter;
import com.app.jude.faceAge.R;

import com.google.gson.Gson;
import com.microsoft.projectoxford.face.contract.Face;

public class ResultActivity extends AppCompatActivity {

    String data;
    byte[] byteArray;
    public View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

         data = getIntent().getStringExtra("list_faces");
         AudienceNetworkAds.facebookInterstitialAd(this);


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

    private void makeToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }


}

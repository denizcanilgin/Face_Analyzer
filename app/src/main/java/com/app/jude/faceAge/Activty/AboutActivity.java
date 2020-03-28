package com.app.jude.faceAge.Activty;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.app.jude.faceAge.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class AboutActivity extends AppCompatActivity {

    int verCode = 1;
    TextView tv_versionCode;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        tv_versionCode = findViewById(R.id.tv_verCode);

        try {
            String versionName = getApplicationContext().getPackageManager()
                    .getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
            tv_versionCode.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



//        try {
//            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
//            String version = pInfo.versionName;
//            verCode = pInfo.versionCode;
//            tv_versionCode.setText(verCode);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }









    }
}

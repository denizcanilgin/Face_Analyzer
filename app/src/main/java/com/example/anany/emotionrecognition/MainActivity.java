package com.example.anany.emotionrecognition;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Face;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import cdflynn.android.library.checkview.CheckView;

public class MainActivity extends AppCompatActivity {

    Button process, takePicture, bt_pickImageFromGallery;
    ImageView imageView, hidden;

    LinearLayout ly_beforePick, ly_beforePickGallery;

    CheckView checkViewTakePhoto, checkViewPickImage;

    private FaceServiceClient faceServiceClient;
    Bitmap mBitmap;
    Boolean ready = false;

    @Override
    protected void onResume() {
        super.onResume();
//        makeToast("onResume");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //IMPORTANT!!------------------------------------------------------------------------------
        //Replace the below tags <> with your own endpoint and API Subscription Key.
        //For help with this, read the project's README file.
        faceServiceClient = new FaceServiceRestClient("https://centralus.api.cognitive.microsoft.com/face/v1.0", "80b2933091ad43619931f360804b9924");
        ly_beforePickGallery = findViewById(R.id.ly_beforePickGallery);
        ly_beforePick = findViewById(R.id.ly_beforePick);
        checkViewTakePhoto = findViewById(R.id.checkTakePhoto);
        checkViewPickImage = findViewById(R.id.checkPickImage);
        bt_pickImageFromGallery = findViewById(R.id.bt_pickImageFromGallery);

        takePicture = findViewById(R.id.takePic);
        imageView = findViewById(R.id.imageView);
        imageView.setVisibility(View.INVISIBLE);

        process = findViewById(R.id.processClick);
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 100);
                }
            }
        });

        bt_pickImageFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 101);
                }
            }


        });

        process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
//                        R.drawable.murat2);
//
//

//

                //detectandFrame(mBitmap);

                if (ready) {

                        detectandFrame(mBitmap);


                } else {
                    makeToast(getString(R.string.please_take_pic));
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Image Picked
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {

                imagePicked();
                final Uri imageUri = data.getData();
                final InputStream imageStream;
                try {

                    this.mBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    ready = true;

                }catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }

        //Photo Taken
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {

                //Check photo is taken
                photoTaken();

                mBitmap = (Bitmap) data.getExtras().get("data");
                // imageView.setImageBitmap(mBitmap);
                ready = true;
            }
        }
    }

    private void detectandFrame(final Bitmap mBitmap)  {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream((outputStream.toByteArray()));

        AsyncTask<InputStream, String, Face[]> detectTask = new AsyncTask<InputStream, String, Face[]>() {
            ProgressDialog pd = new ProgressDialog(MainActivity.this);

            @Override
            protected Face[] doInBackground(InputStream... inputStreams) {

                publishProgress("Detecting...");
                Log.i("progress:","Detecting...");
                //This is where you specify the FaceAttributes to detect. You can change this for your own use.
                FaceServiceClient.FaceAttributeType[] faceAttr = new FaceServiceClient.FaceAttributeType[]{
                        FaceServiceClient.FaceAttributeType.HeadPose,
                        FaceServiceClient.FaceAttributeType.Age,
                        FaceServiceClient.FaceAttributeType.Gender,
                        FaceServiceClient.FaceAttributeType.Emotion,
                        FaceServiceClient.FaceAttributeType.FacialHair,
                        FaceServiceClient.FaceAttributeType.Makeup


                };

                try {
                    Face[] result = faceServiceClient.detect(inputStreams[0],
                            true,
                            false,
                            faceAttr);

                    if (result == null) {
                        publishProgress("Detection failed. Nothing detected.");
                        Log.i("progress:","Failed");

                    }

                    publishProgress(String.format("Detection Finished. %d face(s) detected", result.length));
                    Log.i("progress:","Finished... %d face(s) detected" + result.length);

                    return result;
                } catch (Exception e) {
                    publishProgress("Detection Failed: " + e.getMessage());
                    Log.i("progress:","Failed..." + e.getMessage());

                    return null;
                }


            }

            @Override
            protected void onPreExecute() {
                pd.show();
            }

            @Override
            protected void onProgressUpdate(String... values) {
                pd.setMessage(values[0]);
            }

            @Override
            protected void onPostExecute(Face[] faces) {
                pd.dismiss();
                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                Gson gson = new Gson();
                String data = gson.toJson(faces);
                if (faces == null || faces.length == 0) {
                    noFaceDetected();
                } else {
                    intent.putExtra("list_faces", data);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    intent.putExtra("image", byteArray);

                    try {
                        startActivity(intent);
                    }catch (Exception e){}

                }

            }
        };
        detectTask.execute(inputStream);
    }

    private void makeToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    private void noFaceDetected() {
        makeToast(getString(R.string.no_face_detected));

        //For Photo
        ly_beforePick.setVisibility(View.VISIBLE);
        checkViewTakePhoto.setVisibility(View.INVISIBLE);

        ly_beforePickGallery.setVisibility(View.VISIBLE);
        checkViewPickImage.setVisibility(View.INVISIBLE);

        //For Image

    }



    private void photoTaken() {

        ly_beforePick.setVisibility(View.INVISIBLE);
        checkViewTakePhoto.setVisibility(View.VISIBLE);
        checkViewTakePhoto.check();

        ly_beforePickGallery.setVisibility(View.VISIBLE);
        checkViewPickImage.setVisibility(View.INVISIBLE);
    }

    private void imagePicked() {

        ly_beforePickGallery.setVisibility(View.INVISIBLE);
        checkViewPickImage.setVisibility(View.VISIBLE);
        checkViewPickImage.check();

        ly_beforePick.setVisibility(View.VISIBLE);
        checkViewTakePhoto.setVisibility(View.INVISIBLE);

    }

    public Bitmap makeSmallerImage(Bitmap image, int maximumSize) {

        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;

        if (bitmapRatio > 1) {
            width = maximumSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maximumSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}

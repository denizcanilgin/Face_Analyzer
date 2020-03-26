package com.example.anany.emotionrecognition;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.FaceRectangle;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class CustomAdapter extends BaseAdapter {

    private Face[] faces;
    private Context context;
    private LayoutInflater inflater;
    private Bitmap orig;

    public CustomAdapter(Face[] faces, Context context, Bitmap orig) {
        this.faces = faces;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.orig = orig;
    }


    @Override
    public int getCount() {
        return faces.length;
    }

    @Override
    public Object getItem(int position) {
        return faces[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = inflater.inflate(R.layout.listview_layout, null);
        }

        TextView age, gender, facialhair, headpose, smile;

        ImageView imageView;

        age = view.findViewById(R.id.textAge);
        gender = view.findViewById(R.id.textGender);
        facialhair = view.findViewById(R.id.textFacialHair);
        headpose = view.findViewById(R.id.textHeadPose);
        smile = view.findViewById(R.id.textSmile);

        imageView = view.findViewById(R.id.imgThumb);

        age.setText("Age: " + faces[position].faceAttributes.age);
        gender.setText("Gender: " + faces[position].faceAttributes.gender);

        if(faces[position].faceAttributes.gender.equals("male")){

            String m = FHAnalyze(faces[position].faceAttributes.facialHair.moustache);
            String b =  FHAnalyze(faces[position].faceAttributes.facialHair.beard);
            String s =  FHAnalyze(faces[position].faceAttributes.facialHair.sideburns);

            String fh = context.getString(R.string.moustache) + m + "\n" + context.getString(R.string.beard) + b + "\n" + context.getString(R.string.sideburns) + s;

            facialhair.setText(fh);


        }else {
            String mpEye = "";
            String mpLip = "";
            if(faces[position].faceAttributes.makeup.eyeMakeup)
                mpEye = context.getString(R.string.yes);
            else
                mpEye = context.getString(R.string.no);

            if(faces[position].faceAttributes.makeup.lipMakeup)
                mpLip = context.getString(R.string.yes);
            else
                mpLip = context.getString(R.string.no);



            facialhair.setText(context.getString(R.string.eye_makeup) + mpEye + "\n" + context.getString(R.string.lip_makeup) + mpLip );


        }


        headpose.setText(String.format("Head Pose: %f %f %f", faces[position].faceAttributes.headPose.pitch,
                faces[position].faceAttributes.headPose.yaw,
                faces[position].faceAttributes.headPose.roll));


        TreeMap<Double, String> treeMap = new TreeMap<>();
        treeMap.put(faces[position].faceAttributes.emotion.happiness, context.getString(R.string.happiness));
        treeMap.put(faces[position].faceAttributes.emotion.anger, context.getString(R.string.anger));
        treeMap.put(faces[position].faceAttributes.emotion.disgust, context.getString(R.string.disgust));
        treeMap.put(faces[position].faceAttributes.emotion.sadness, context.getString(R.string.sadness));
        treeMap.put(faces[position].faceAttributes.emotion.neutral, context.getString(R.string.neutral));
        treeMap.put(faces[position].faceAttributes.emotion.surprise, context.getString(R.string.surprise));
        treeMap.put(faces[position].faceAttributes.emotion.fear, context.getString(R.string.fear));

        ArrayList<Double> arrayList = new ArrayList<>();
        TreeMap<Integer, String> rank = new TreeMap<>();

        int counter = 0;
        for (Map.Entry<Double, String> entry : treeMap.entrySet()) {
            String key = entry.getValue();
            Double value = entry.getKey();
            rank.put(counter, key);
            counter++;
            arrayList.add(value);
        }

        smile.setText(rank.get(rank.size() - 1) + ": " + 100 * arrayList.get(rank.size() - 1) + "% " + rank.get(rank.size() - 2) + ": " + 100 * arrayList.get(rank.size() - 2) + "%");
        FaceRectangle faceRectangle = faces[position].faceRectangle;
        Bitmap bitmap = Bitmap.createBitmap(orig, faceRectangle.left, faceRectangle.top, faceRectangle.width, faceRectangle.height);

        imageView.setImageBitmap(bitmap);
        imageView.setImageBitmap(bitmap);
        return view;
    }

    public String FHAnalyze(Double value) {
        String result = "";

        if ((value < 0.1)) result = context.getString(R.string.very_light);
        if ((value >= 0.1) && (value < 0.4)) result = context.getString(R.string.light);
        if ((value >= 0.4) && (value < 0.5)) result = context.getString(R.string.normal);
        if ((value >= 0.5) && (value < 0.8)) result = context.getString(R.string.heavy);
        if ((value >= 0.8) && (value <= 1.0)) result = context.getString(R.string.very_heavy);


        return result;
    }

}

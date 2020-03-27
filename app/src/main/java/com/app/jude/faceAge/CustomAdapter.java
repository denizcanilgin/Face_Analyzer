package com.app.jude.faceAge;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.FaceRectangle;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class CustomAdapter extends BaseAdapter {

    private Face[] faces;
    private Context context;
    private LayoutInflater inflater;
    private Bitmap orig;

    ImageView deneme;
    Button bt_share;
    View converView;

    ArrayList<CardView> cardViewArrayList;
    ArrayList<Button> buttonArrayList;


    public CustomAdapter(Face[] faces, Context context, Bitmap orig) {
        this.faces = faces;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.orig = orig;
        cardViewArrayList = new ArrayList<>();
        buttonArrayList = new ArrayList<>();
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
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view = convertView;
        this.converView = converView;


        if (convertView == null) {
            view = inflater.inflate(R.layout.listview_layout, null);
        }

        TextView age, gender, facialhair, headpose, smile, glasses;

        ImageView imageView;




        deneme = view.findViewById(R.id.deneme);
        bt_share = view.findViewById(R.id.bt_share);
        CardView card_view = view.findViewById(R.id.card_view_list);
        cardViewArrayList.add(card_view);
        buttonArrayList.add(bt_share);
        bt_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buttonArrayList.get(position).setText("FaceAge - how old do i look like");
                shareIntent(screenShot(cardViewArrayList.get(position)));
                buttonArrayList.get(position).setText("share");


            }
        });


        glasses = view.findViewById(R.id.textGlassess);
        age = view.findViewById(R.id.textAge);
        gender = view.findViewById(R.id.textGender);
        facialhair = view.findViewById(R.id.textFacialHair);
        headpose = view.findViewById(R.id.textHeadPose);
        smile = view.findViewById(R.id.textSmile);

        imageView = view.findViewById(R.id.imgThumb);

        age.setText("Age: " + faces[position].faceAttributes.age);
        gender.setText("Gender: " + faces[position].faceAttributes.gender);
        glasses.setText("" + faces[position].faceAttributes.glasses );

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

        Log.i("Happiness", "" + faces[position].faceAttributes.emotion.happiness);
        Log.i("Anger", "" + faces[position].faceAttributes.emotion.anger);
        Log.i("Disgust", "" + faces[position].faceAttributes.emotion.disgust);
        Log.i("Sadness", "" + faces[position].faceAttributes.emotion.sadness);
        Log.i("Neutral", "" + faces[position].faceAttributes.emotion.neutral);
        Log.i("Surprise", "" + faces[position].faceAttributes.emotion.surprise);
        Log.i("Fear", "" + faces[position].faceAttributes.emotion.fear);

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


        EmojiUniCodes emojiUniCodes = new EmojiUniCodes();

        String emoji1 = pickEmoji(rank.get(rank.size() - 1));
        String emoji2 = pickEmoji(rank.get(rank.size() - 2));

        int value1 = (int) (100 * arrayList.get(rank.size() - 1));
        int value2 = (int) (100 * arrayList.get(rank.size() - 2));

        pickEmoji(rank.get(rank.size() - 1));
        smile.setText(emoji1 + rank.get(rank.size() - 1) + ": " + value1 + "% "  + emoji2 + rank.get(rank.size() - 2) + ": " + value2 + "%");
        Log.i("current_emotion1","" + rank.get(rank.size() - 1));
        Log.i("current_emotion2","" + rank.get(rank.size() - 2));


        FaceRectangle faceRectangle = faces[position].faceRectangle;
        Bitmap bitmap = Bitmap.createBitmap(orig, faceRectangle.left, faceRectangle.top, faceRectangle.width, faceRectangle.height);

        imageView.setImageBitmap(bitmap);
        imageView.setImageBitmap(bitmap);
        return view;
    }

    //Facial Hair Analyze Method
    public String FHAnalyze(Double value) {
        String result = "";

        if ((value < 0.1)) result = context.getString(R.string.very_light);
        if ((value >= 0.1) && (value < 0.4)) result = context.getString(R.string.light);
        if ((value >= 0.4) && (value < 0.5)) result = context.getString(R.string.normal);
        if ((value >= 0.5) && (value < 0.8)) result = context.getString(R.string.heavy);
        if ((value >= 0.8) && (value <= 1.0)) result = context.getString(R.string.very_heavy);
        return result;
    }

    public String pickEmoji(String state){
        String emoji = "";
        EmojiUniCodes emojiUniCodes = new EmojiUniCodes();

         if(state.equals(context.getString(R.string.happiness)))
            emoji = emojiUniCodes.getEmojiByUnicode(emojiUniCodes.toohappy);
         if(state.equals(context.getString(R.string.sadness)))
            emoji = emojiUniCodes.getEmojiByUnicode(emojiUniCodes.sad);
         if(state.equals(context.getString(R.string.neutral)))
             emoji = emojiUniCodes.getEmojiByUnicode(emojiUniCodes.neutral);
         if(state.equals(context.getString(R.string.fear)))
             emoji = emojiUniCodes.getEmojiByUnicode(emojiUniCodes.fear);
         if(state.equals(context.getString(R.string.surprise)))
             emoji = emojiUniCodes.getEmojiByUnicode(emojiUniCodes.surprised);
         if(state.equals(context.getString(R.string.disgust)))
             emoji = emojiUniCodes.getEmojiByUnicode(emojiUniCodes.disgust);
         if(state.equals(context.getString(R.string.anger)))
             emoji = emojiUniCodes.getEmojiByUnicode(emojiUniCodes.angry);


        return emoji;
    }

    public Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }


    private void shareIntent(Bitmap bitmap){

        String bitmapPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap,"title", null);
        Uri bitmapUri = Uri.parse(bitmapPath);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
        context.startActivity(Intent.createChooser(intent, "Share"));

    }

}

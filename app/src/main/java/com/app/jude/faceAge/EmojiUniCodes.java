package com.app.jude.faceAge;

public class EmojiUniCodes {

    int emoji1 = 0x1F60A;



    int toohappy = 0x1F923;
    int sohappy = 0x1F602;
    int smiling = 0x1F642;
    int neutral = 0x1F610;
    int sad = 0x1F614;
    int sosad = 0x1F62A;
    int angry = 0x1F621;
    int fear = 0x1F628;
    int disgust = 0x1F922;

    int smiling_wtih_glasses = 0x1F913;
    int surprised = 0x1F62E;




    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

}

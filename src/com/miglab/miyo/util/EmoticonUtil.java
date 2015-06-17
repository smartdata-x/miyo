package com.miglab.miyo.util;

import android.content.Context;
import android.content.res.AssetManager;
import com.miglab.miyo.entity.Emoticon;
import com.miglab.miyo.entity.EmoticonPageInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/6/16.
 */
public class EmoticonUtil {
    public static String[] faceString = {
            "emoticon_0.png",
            "emoticon_1.png",
            "emoticon_2.png",
            "emoticon_3.png",
            "emoticon_4.png",
            "emoticon_5.png",
            "emoticon_6.png",
            "emoticon_7.png",
            "emoticon_8.png",
            "emoticon_9.png",
            "emoticon_10.png",
            "emoticon_11.png",
            "emoticon_12.png",
            "emoticon_13.png",
            "emoticon_14.png",
            "emoticon_15.png",
            "emoticon_16.png",
            "emoticon_17.png",
            "emoticon_18.png",
            "emoticon_19.png",
            "emoticon_20.png",
            "emoticon_21.png",
            "emoticon_22.png",
            "emoticon_23.png",
            "emoticon_24.png",
            "emoticon_25.png",
            "emoticon_26.png",
            "emoticon_27.png",
            "emoticon_28.png",
            "emoticon_29.png",
            "emoticon_30.png",
            "emoticon_31.png",
            "emoticon_32.png",
            "emoticon_33.png",
            "emoticon_34.png",
            "emoticon_35.png",
            "emoticon_36.png",
            "emoticon_37.png",
            "emoticon_38.png",
            "emoticon_39.png",
            "emoticon_40.png",
            "emoticon_41.png",
            "emoticon_42.png",
            "emoticon_43.png",
            "emoticon_44.png",
            "emoticon_45.png",
            "emoticon_46.png",
            "emoticon_47.png",
            "emoticon_48.png",
            "emoticon_49.png",
            "emoticon_50.png",
            "emoticon_51.png",
            "emoticon_52.png",
            "emoticon_53.png",
            "emoticon_54.png",
            "emoticon_55.png",
            "emoticon_56.png",
            "emoticon_57.png",
            "emoticon_58.png",
            "emoticon_59.png",
            "emoticon_60.png",
            "emoticon_61.png",
            "emoticon_62.png",
            "emoticon_63.png",
            "emoticon_64.png",
            "emoticon_65.png",
            "emoticon_66.png",
            "emoticon_67.png",
            "emoticon_68.png",
            "emoticon_69.png",
            "emoticon_70.png",
            "emoticon_71.png",
            "emoticon_72.png",
            "emoticon_73.png",
            "emoticon_74.png",
            "emoticon_75.png",
            "emoticon_76.png",
            "emoticon_77.png",
            "emoticon_78.png",
            "emoticon_79.png",
            "emoticon_80.png",
            "emoticon_82.png",
            "emoticon_82.png",
            "emoticon_83.png",
            "emoticon_84.png",
            "emoticon_85.png",
            "emoticon_86.png",
            "emoticon_87.png",
            "emoticon_88.png",
            "emoticon_89.png"
    };


        private static String getEmoticonKey(String s) {
                return "[\\" + s.substring(s.indexOf("_")+1,s.indexOf(".")) + "]";
        }

        public static List<EmoticonPageInfo> getEmoticonPageInfoListFromAsset(Context con) {
                List<EmoticonPageInfo> list = new ArrayList<>();
                List<Emoticon> emoticonList = new ArrayList<>();
                AssetManager assetManager = con.getAssets();
                try {
                       String[] faceString =  assetManager.list("face");
                        for(int i=0; i<faceString.length; i++){
                                String s = faceString[i];
                                Emoticon emoticon = new Emoticon(Emoticon.FACE_TYPE_NOMAL,"assets://face/" + s,getEmoticonKey(s));
                                emoticonList.add(emoticon);
                        }
                } catch (IOException e) {
                        e.printStackTrace();
                }
                EmoticonPageInfo info = new EmoticonPageInfo("经典",3,7,"assets://icon_emoji.png","逗比", true, 3, 5, 5, emoticonList);
                list.add(info);
                return list;
        }
}

package cn.face.sign;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.google.android.exoplayer2.util.Log;

import java.util.HashMap;


public class RLFaceSignModule extends ReactContextBaseJavaModule {
    public static String TAG = "RLFaceSignModule";

    public RLFaceSignModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "RLFaceSignModule";//这里定义的是该模块名字
    }


    @ReactMethod
    public void startActivityFromJS(String name, ReadableMap readableMap) {//该模块的方法
        Log.e(TAG, "===== readableMap >>>> " + readableMap);
        HashMap<String, Object> map = readableMap.toHashMap();

        try {
            Activity currentActivity = getCurrentActivity();
            if (null != currentActivity) {
                Class toActivity = Class.forName("cn.face.sign." + name);
                Intent intent = new Intent(currentActivity, toActivity);
//                intent.putExtra("params", params);
//                for (String key : map.keySet()) {
//                    Log.e(TAG, "===== key >>>> " + key);
//                    Log.e(TAG, "===== map.get(key) >>>> " + map.get(key));
////                    intent.putExtra(key + "", (Bundle) map.get(key));
//                }
                currentActivity.startActivity(intent);
            }
        } catch (Exception e) {
            throw new JSApplicationIllegalArgumentException(
                    "不能打开Activity : " + e.getMessage());
        }
    }

    @ReactMethod
    public void dataToJS(Callback successBack, Callback errorBack) {
        try {
            Activity currentActivity = getCurrentActivity();
            String result = currentActivity.getIntent().getStringExtra("data");
            if (TextUtils.isEmpty(result)) {
                result = "没有数据";
            }
            successBack.invoke(result);
        } catch (Exception e) {
            errorBack.invoke(e.getMessage());
        }
    }
}

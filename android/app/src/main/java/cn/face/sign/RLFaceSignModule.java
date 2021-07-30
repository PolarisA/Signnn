package cn.face.sign;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;


import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.google.android.exoplayer2.util.Log;
import com.signnn.R;
import com.yuntongxun.plugin.FaceSignKit;
import com.yuntongxun.plugin.common.common.utils.EasyPermissionsEx;
import com.yuntongxun.plugin.common.common.utils.ToastUtil;

import java.util.HashMap;


public class RLFaceSignModule extends ReactContextBaseJavaModule {
    public static String TAG = "RLFaceSignModule";
    public static final int REQUEST_PERMISSION = 0x603;

    public static String appointNo;
    public static String phone;
    public static int ectId = 1;
    public static String signUrl;


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
        for (String key : map.keySet()) {
//            Log.e(TAG, "===== key >>>> " + key);
//            Log.e(TAG, "===== map.get(key) >>>> " + map.get(key));

            switch (key) {
                case "phone":
                    phone = map.get(key).toString();
                    break;
                case "appointNo":
                    appointNo = map.get(key).toString();
                    break;
                case "ectId":
                    ectId = (Integer) map.get(key);
                    break;
                case "signUrl":
                    signUrl = map.get(key).toString();
                    break;
                default:
                    break;
            }
        }

        Log.e(TAG, "===== startActivityFromJS ectId >>>> " + ectId);
        if (FaceSignKit.isActive()) {
            handleLoginResult(ectId);
        } else {
            doHandleLoginService();
        }
    }

    private void handleLoginResult(int ectId) {
        if (!EasyPermissionsEx.hasPermissions(
                getCurrentActivity(),
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE)) {
            EasyPermissionsEx.requestPermissions(
                    getCurrentActivity(),
                    String.valueOf(R.string.rlytx_permission_tips),
                    REQUEST_PERMISSION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE);
            return;
        }
        startFaceSign(ectId);
    }

    private void doHandleLoginService() {
        Log.e(TAG, "===== doHandleLoginService >>>> ");
        if (!EasyPermissionsEx.hasPermissions(
                getCurrentActivity(),
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE)) {
            EasyPermissionsEx.requestPermissions(
                    getCurrentActivity(),
                    String.valueOf(R.string.rlytx_permission_tips),
                    REQUEST_PERMISSION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE);
            return;
        }
        startLoginSignServer();
    }

    private void startLoginSignServer() {
        android.util.Log.e(TAG, "===== startLoginSignServer >>>> ");

        FaceSignKit.Builder builder = new FaceSignKit.Builder()
                .setAccount(phone)
                .setUrl(signUrl);

        FaceSignKit.getEngine().login(builder, (errorCode, errorMsg) -> {
            Log.e(TAG, "===== FaceSignKit login >>>> " + errorCode + errorMsg);
            if (errorCode != 200) {
                ToastUtil.show(errorMsg);
            }
            ToastUtil.showMessage("登录成功");
            handleLoginResult(ectId);
        });
    }

    private void startFaceSign(int execId) {
        Log.e(TAG, "===== startFaceSign >>>> " + execId);
        FaceSignKit.getEngine()
                .startFaceSign(
                        getCurrentActivity(),
                        execId,
                        "123456",
                        new FaceSignKit.OnFaceSignListener() {
                            @Override
                            public void onFaceSign(int errorCode, String errorMsg) {
                                Log.e(TAG, "====== onFaceSign >>>>>>" + errorCode + "===== errorMsg >>" + errorMsg);
                                if (errorCode != 200) {
                                    ToastUtil.show(errorMsg);
                                }
                            }

                            @Override
                            public void onStartFaceSigning() {
                                Log.e(TAG, "====== onStartFaceSigning >>>>>>");
                            }
                        });
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

//        try {
//            Activity currentActivity = getCurrentActivity();
//            if (null != currentActivity) {
//                Class toActivity = Class.forName("cn.face.sign." + name);
//                Intent intent = new Intent(currentActivity, toActivity);
//                intent.putExtra("phone", phone);
//                intent.putExtra("appointNo", appointNo);
//                intent.putExtra("ectId", ectId);
//                intent.putExtra("signUrl", signUrl);
//
////                currentActivity.startActivity(intent);
//            }
//        } catch (Exception e) {
//            throw new JSApplicationIllegalArgumentException(
//                    "不能打开Activity : " + e.getMessage());
//        }
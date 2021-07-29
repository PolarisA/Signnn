package cn.face.sign;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.signnn.R;
import com.yuntongxun.plugin.FaceSignKit;
import com.yuntongxun.plugin.common.common.BackwardSupportUtil;
import com.yuntongxun.plugin.common.common.utils.EasyPermissionsEx;

public class RLFaceSignActivity extends AppCompatActivity {
    public static final int REQUEST_PERMISSION = 0x603;
    public static String TAG = "RLFaceSignActivity";
    private int mExecId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facesgin_rl);

        Log.e(TAG, "====== getApplicationContext() >>>>>>" + getApplicationContext());
        FaceSignKit.init(getApplicationContext());

        Log.e(TAG, "===== FaceSignKit >>>> " + FaceSignKit.isActive());
        if (FaceSignKit.isActive()) {
            handleLoginResult();
        } else {
            doHandleLoginService();
        }
    }

//    @Override
//    public int getLayoutId() {
//        return R.layout.activity_facesgin_rl;
//    }

    private void doHandleLoginService() {
        Log.e(TAG, "===== doHandleLoginService >>>> ");
        if (!EasyPermissionsEx.hasPermissions(this,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE)) {
            EasyPermissionsEx.requestPermissions(RLFaceSignActivity.this, getString(R.string.rlytx_permission_tips),
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
        Log.e(TAG, "===== startLoginSignServer >>>> ");

        new AlertDialog.Builder(RLFaceSignActivity.this)
                .setTitle("离开页面提示")
                .setMessage("存在未保存的数据，若离开此页面未保存的数据会清空，确认离开此页面吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Intent intent = new Intent(context, RegionInfoActivity.class);
//                        startActivityForResult(intent, UPDATE_REGION);
                        Log.e(TAG, "===== dialogInterface >>>> " + dialogInterface);

                        FaceSignKit.Builder builder = new FaceSignKit.Builder()
                                .setAccount("18888888888")
                                .setUrl("https://shuanglu.dgwfund.cn");

                        Log.e(TAG, "===== builder >>>> " + builder);

                        FaceSignKit.getEngine().login(builder, (errorCode, errorMsg) -> {
                            Log.e(TAG, "===== FaceSignKit login >>>> " + errorCode + errorMsg);
                            handleLoginResult();
                        });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e(TAG, "===== Cancel dialogInterface >>>> " + dialogInterface);
                    }
                }).show();

//        showPostingDialog("正在登录，请稍后...");
    }

    private void handleLoginResult() {
        if (!EasyPermissionsEx.hasPermissions(this,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE)) {
            EasyPermissionsEx.requestPermissions(RLFaceSignActivity.this, getString(R.string.rlytx_permission_tips), REQUEST_PERMISSION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE);
        }
        String execId = "1";
        mExecId = BackwardSupportUtil.getInt(execId, 0);

        Log.e(TAG, "====== mExecId >>>>>>" + mExecId);
        mExecId = 1;
        startFaceSign(mExecId);
    }

    private void startFaceSign(int execId) {
        FaceSignKit.getEngine().startFaceSign(RLFaceSignActivity.this, execId, "123456", new FaceSignKit.OnFaceSignListener() {
            @Override
            public void onFaceSign(int errorCode, String errorMsg) {
//                                dismissDialog();
            }

            @Override
            public void onStartFaceSigning() {
//                                showPostingDialog();
            }
        });
    }

}
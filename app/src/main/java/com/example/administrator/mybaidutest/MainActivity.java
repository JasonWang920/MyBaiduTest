package com.example.administrator.mybaidutest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.acker.simplezxing.activity.CaptureActivity;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.api.track.ClearCacheTrackRequest;
import com.baidu.trace.api.track.ClearCacheTrackResponse;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.HistoryTrackResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.TrackPoint;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ImageUtil;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // 请求标识
    int tag = 1;
    // 轨迹服务ID
    long serviceId = 138876;
    // 设备标识
    String entityName = "myCar2";
    // 是否需要对象存储服务，默认为：false，关闭对象存储服务。注：鹰眼 Android SDK v3.0以上版本支持随轨迹上传图像等对象数据，若需使用此功能，该参数需设为 true，且需导入bos-android-sdk-1.0.2.jar。
    boolean isNeedObjectStorage = false;

    // 初始化轨迹服务
    Trace mTrace = null;
    // 初始化轨迹服务客户端
    LBSTraceClient mTraceClient=null;
    // 定位周期(单位:秒)
    int gatherInterval = 5;
    // 打包回传周期(单位:秒)
    int packInterval = 10;

    private static final int REQ_CODE_PERMISSION = 0x1111;

    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;

    /**
     * 选择系统图片Request Code
     */
    public static final int REQUEST_IMAGE = 112;

    TextView textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTraceClient  = new LBSTraceClient(getApplicationContext());
        mTrace=new Trace(serviceId, entityName, isNeedObjectStorage);
        // 设置定位和打包周期
        mTraceClient.setInterval(gatherInterval, packInterval);
        textview= (TextView) findViewById(R.id.textView);

        //初始化
        ZXingLibrary.initDisplayOpinion(this);

    }

    public void startService(View view) {
// 开启服务
        mTraceClient.startTrace(mTrace, mTraceListener);
    }
    public void startLocation(View view) {
// 开启采集
        mTraceClient.startGather(mTraceListener);
    }

    // 初始化轨迹服务监听器
    OnTraceListener mTraceListener = new OnTraceListener() {
        // 开启服务回调
        @Override
        public void onStartTraceCallback(int status, String message) {
            Toast.makeText(MainActivity.this,"开启服务了,status:"+status+",message:"+message,Toast.LENGTH_SHORT).show();
        }
        // 停止服务回调
        @Override
        public void onStopTraceCallback(int status, String message) {
            Toast.makeText(MainActivity.this,"停止服务了,status,"+status+",message"+message,Toast.LENGTH_SHORT).show();
        }
        // 开启采集回调
        @Override
        public void onStartGatherCallback(int status, String message) {
            Toast.makeText(MainActivity.this,"开启采集了，status"+status+"message"+message,Toast.LENGTH_SHORT).show();
        }
        // 停止采集回调
        @Override
        public void onStopGatherCallback(int status, String message) {
            Toast.makeText(MainActivity.this,"停止了采集了status，"+ status+"message"+message,Toast.LENGTH_SHORT).show();
        }
        // 推送回调
        @Override
        public void onPushCallback(byte messageNo, PushMessage message) {
            Toast.makeText(MainActivity.this,"推送了回调+messageNo"+messageNo+"message"+message,Toast.LENGTH_SHORT).show();
        }
    };

    public void stopLocation(View view) {
        // 停止采集
        mTraceClient.stopGather(mTraceListener);

    }

    public void stopService(View view) {
        // 停止服务
        mTraceClient.stopTrace(mTrace, mTraceListener);
    }

    // 创建历史轨迹请求实例
    HistoryTrackRequest historyTrackRequest = new HistoryTrackRequest(tag, serviceId, entityName);

    // 初始化轨迹监听器
    OnTrackListener mTrackListener = new OnTrackListener() {
        // 历史轨迹回调
        @Override
        public void onHistoryTrackCallback(HistoryTrackResponse response) {
            int total = response.getTotal();
            List<TrackPoint> points=null;
            StringBuffer history=new StringBuffer();
            if (total!=0){
                 points= response.getTrackPoints();
                history.append("总共有"+total+"条数据"+"，size"+ response.getSize());
                for (int i = 0; i <points.size() ; i++) {
                    Log.i("aaa", "onHistoryTrackCallback: "+"历史记录："+points.get(i).toString());
                    history.append(points.get(i).toString()+"\n");
                }
                textview.setText(history);
            }
        }

        @Override
        public void onClearCacheTrackCallback(ClearCacheTrackResponse clearCacheTrackResponse) {
            super.onClearCacheTrackCallback(clearCacheTrackResponse);
            Toast.makeText(MainActivity.this,clearCacheTrackResponse.toString(),Toast.LENGTH_SHORT).show();
        }
    };

    public void check(View view) {
        //设置轨迹查询起止时间
// 开始时间(单位：秒)
        long startTime = System.currentTimeMillis() / 1000 - 12 * 60 * 60;
        // 结束时间(单位：秒)
        long endTime = System.currentTimeMillis() / 1000;
// 设置开始时间
        historyTrackRequest.setStartTime(startTime);
// 设置结束时间
        historyTrackRequest.setEndTime(endTime);
// 查询历史轨迹
        mTraceClient.queryHistoryTrack(historyTrackRequest, mTrackListener);
    }

    public void clear(View view) {
        List<String> entityNames = new ArrayList<>();
        entityNames.add(entityName);
        ClearCacheTrackRequest request = new ClearCacheTrackRequest(tag,serviceId,entityNames);
        mTraceClient.clearCacheTrack(request,mTrackListener);
    }

    public void jump(View view) {

        // Open Scan Activity
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Do not have the permission of camera, request it.
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, REQ_CODE_PERMISSION);
        } else {
            // Have gotten the permission
            startCaptureActivityForResult();
        }


    }


    private void startCaptureActivityForResult() {
        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(CaptureActivity.KEY_NEED_BEEP, CaptureActivity.VALUE_BEEP);
        bundle.putBoolean(CaptureActivity.KEY_NEED_VIBRATION, CaptureActivity.VALUE_VIBRATION);
        bundle.putBoolean(CaptureActivity.KEY_NEED_EXPOSURE, CaptureActivity.VALUE_NO_EXPOSURE);
        bundle.putByte(CaptureActivity.KEY_FLASHLIGHT_MODE, CaptureActivity.VALUE_FLASHLIGHT_OFF);
        bundle.putByte(CaptureActivity.KEY_ORIENTATION_MODE, CaptureActivity.VALUE_ORIENTATION_AUTO);
        intent.putExtra(CaptureActivity.EXTRA_SETTING_BUNDLE, bundle);
        startActivityForResult(intent, CaptureActivity.REQ_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_CODE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // User agree the permission
                    startCaptureActivityForResult();
                } else {
                    // User disagree the permission
                    Toast.makeText(this, "You must agree the camera permission request before you use the code scan function", Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //这是第一个简单的跳转
        switch (requestCode) {
            case CaptureActivity.REQ_CODE:
                switch (resultCode) {
                    //返回的结果
                    case RESULT_OK:
                        textview.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));  //or do sth
                        break;
                    case RESULT_CANCELED:
                        if (data != null) {
                            // for some reason camera is not working correctly
                            textview.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                        }
                        break;

                }
                break;
        }

        /**
         * 跳转到  Zxing_libary的
         */
        //这是跳转到第二个扫描的返回
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this, "解析结果222:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    /**
      *         /**
      * 处理二维码扫描结果
      */


    public void jump2(View view) {

        // Open Scan Activity
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Do not have the permission of camera, request it.
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, REQ_CODE_PERMISSION);
        } else {
            // Have gotten the permission
            Intent intent = new Intent(MainActivity.this, com.uuzuche.lib_zxing.activity.CaptureActivity.class);
            startActivityForResult(intent, REQUEST_CODE);

        }


    }




}

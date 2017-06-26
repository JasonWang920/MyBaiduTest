MyBaiduTest项目中 有集成一个百度鹰眼  
 一个简单的二维码扫描  
还有一个完整的二维码扫描的按钮（Zxing_libary）
参考  https://github.com/yipianfengye/android-zxingLibrary

 是通过导module  lib_Zxing;（或者是引用jar 改一改activity就行）
在Java中有个CaptureActivity 二维码扫描的界面；
在需要使用的地方 
  然后     
   // Open Scan Activity
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Do not have the permission of camera, request it.
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, REQ_CODE_PERMISSION);
        } else {
            // Have gotten the permission
            Intent intent = new Intent(MainActivity.this, com.uuzuche.lib_zxing.activity.CaptureActivity.class);
            startActivityForResult(intent, REQUEST_CODE);

        }

在获取返回的结果
   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

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


    <!--/二维码的需要权限开始-->
    <uses-permission android:name="android.permission.CAMERA"/>
    <uses-permission android:name="android.permission.FLASHLIGHT" />

    <uses-feature android:name="android.hardware.camera" />
    <uses-feature android:name="android.hardware.camera.autofocus" />

    <uses-permission android:name="android.permission.VIBRATE" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.INTERNET" />

    <!--/二维码的需要权限结束-->

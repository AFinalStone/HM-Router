package com.hm.iou.router;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

/**
 * Created by hjy on 17/12/25.<br>
 */

public class RouteDispatchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent data = getIntent();
        if(data != null && data.getData() != null) {
            Uri uri = data.getData();
            String scheme = uri.getScheme();
            boolean isAllowed = false;
            //scheme必须以hmiou开头
            if(scheme != null && scheme.startsWith("hmiou")) {
                isAllowed = Utils.isInWhiteList(this, uri);
            }
            //进行路由跳转
            if(isAllowed) {
                //先跳转到首页
                Router.getInstance().buildWithUrl(RouteConsts.URL_HOME_PAGE).
                        withString("url", uri.toString()).navigation(this);
            } else {
                //跳转到应用启动页
                PackageManager packageManager = getPackageManager();
                String packageName = getPackageName();
                Intent intent = packageManager.getLaunchIntentForPackage(packageName);
                if(intent != null) {
                    startActivity(intent);
                }
            }
        }
        finish();
    }
}

package com.hm.iou.router;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * Created by hjy on 2018/5/22.
 */

public class PageNotFoundActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.router_activity_page_not_found);

        findViewById(R.id.btn_check_version).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebBrowser(PageNotFoundActivity.this, "https://a.app.qq.com/o/simple.jsp?pkgname=com.hm.iou");
            }
        });
    }

    public static void openWebBrowser(Context context, String url) {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            final ComponentName componentName = intent.resolveActivity(context.getPackageManager());
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
        } else {
            Toast.makeText(context.getApplicationContext(), "请下载浏览器", Toast.LENGTH_SHORT).show();
        }
    }

}

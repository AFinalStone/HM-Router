package com.hm.iou.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.view.Gravity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hm.iou.router.entity.RouteClassInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by hjy on 17/12/12.<br>
 */

public class Utils {

    /**
     * 从assets文件里读取路由配置信息
     *
     * @param context
     * @param fileName
     * @return
     */
    public static Map<String, List<RouteClassInfo>> readRouteClassConfigFile(Context context, String fileName) {
        AssetManager manager = context.getAssets();
        try {
            InputStream inputStream = manager.open(fileName);
            return readRouteClassConfigFromInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从InputStream流里读取配置
     *
     * @param inputStream
     * @return
     */
    public static Map<String, List<RouteClassInfo>> readRouteClassConfigFromInputStream(InputStream inputStream) {
        try {
            int length = inputStream.available();
            byte[] buffer = new byte[length];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer);
            Gson gson = new Gson();
            Map<String, List<RouteClassInfo>> map = gson.fromJson(json, new TypeToken<Map<String, List<RouteClassInfo>>>(){}.getType());
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取白名单列表
     *
     * @param context
     * @return
     */
    public static List<String> readWhiteNameConfigFile(Context context, String fileName) {
        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open(fileName);
            int length = inputStream.available();
            byte[] buffer = new byte[length];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer);
            Gson gson = new Gson();
            List<String> list = gson.fromJson(json, new TypeToken<List<String>>(){}.getType());
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从Assets文件里读取路由名的配置信息
     *
     * @param context
     * @param fileName
     * @return
     */
    public static Map<String, String> readRouteNameConfigFile(Context context, String fileName) {
        AssetManager manager = context.getAssets();
        try {
            InputStream inputStream = manager.open(fileName);
            return readRouteNameConfigFromInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从InputStream流里读取路由名的配置信息
     *
     * @param inputStream
     * @return
     */
    public static Map<String, String> readRouteNameConfigFromInputStream(InputStream inputStream) {
        try {
            int length = inputStream.available();
            byte[] buffer = new byte[length];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer);
            Gson gson = new Gson();
            Map<String, String> map = gson.fromJson(json, new TypeToken<Map<String, String>>(){}.getType());
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 打开系统浏览器
     *
     * @param context
     * @param url
     */
    public static void launchBrowser(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            intent.setData(Uri.parse(url));
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            if(!(context instanceof Activity)) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void toast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 是否在白名单之类
     *
     * @param context
     * @param targetUri
     * @return
     */
    public static boolean isInWhiteList(Context context, Uri targetUri) {
        String host = targetUri.getHost();
        String path = targetUri.getPath();
        List<String> whiteNameList = readWhiteNameConfigFile(context, "router_white_list.json");
        if(whiteNameList != null && !whiteNameList.isEmpty()) {
            for(String url : whiteNameList) {
                Uri ruleUri = Uri.parse(url);
                if(host != null && path != null && host.equals(ruleUri.getHost()) &&
                        path.equals(ruleUri.getPath())) {
                    return true;
                }
            }
        }
        return false;
    }

}

package com.hm.iou.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;


import com.hm.iou.router.entity.RouteClassInfo;
import com.hm.iou.router.entity.RouteInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by hjy on 17/12/11.<br>
 */

public class Router {

    private static Router INSTANCE;

    public static void init(Context context) {
        INSTANCE = new Router(context);
    }

    public static Router getInstance() {
        return INSTANCE;
    }

    private Context mContext;

    private Map<String, List<RouteClassInfo>> mRouteClassMap;

    private RouteInterceptor mInterceptor;

    public Router(Context context) {
        this.mContext = context.getApplicationContext();
        initRouteTable();
    }

    /**
     * 初始化路由表
     */
    private void initRouteTable() {
        Map<String, List<RouteClassInfo>> routeClassMap = Utils.readRouteClassConfigFile(mContext,
                "router_class_list.json");

        mRouteClassMap = routeClassMap;
    }

    public void setInterceptor(RouteInterceptor interceptor) {
        mInterceptor = interceptor;
    }

    /**
     * 通过url来构建路由跳转信息，会根据url去查找与之匹配的路由规则
     *
     * @param url 路由跳转url
     * @return
     */
    public RouteInfo buildWithUrl(String url) {
        //如果是http开头的路由地址，则直接用浏览器去打开
        if(url.startsWith("http")) {
            return new RouteInfo(Uri.parse(url), null);
        }
        Uri uri = Uri.parse(url);
        List<String> pathList = uri.getPathSegments();
        //找到第一级路径
        String host = uri.getHost();
        String firstPath = pathList != null && pathList.size() > 0 ? pathList.get(0) : null;
        if(TextUtils.isEmpty(host) || TextUtils.isEmpty(firstPath)) {
            return RouteInfo.newEmptyInstance();
        }
        List<RouteClassInfo> list = mRouteClassMap != null ? mRouteClassMap.get(firstPath) : null;
        if(list == null || list.isEmpty()) {
            return RouteInfo.newEmptyInstance();
        }
        //通过遍历来匹配url
        for(RouteClassInfo routeClassInfo : list) {
            Uri item = Uri.parse(routeClassInfo.url);
            String itemHost = item.getHost();
            String itemPath = item.getPath();
            if(host.equals(itemHost) && uri.getPath().equals(itemPath)) {
                //找到路由表
                return new RouteInfo(uri, routeClassInfo);
            }
        }
        return RouteInfo.newEmptyInstance();
    }

    public void navigation(Context context, RouteInfo routeMeta, int requestCode, NavigationCallback callback) {
        if(mInterceptor != null) {
            boolean intercepted = mInterceptor.onIntercept(routeMeta);
            if(intercepted) {
                return;
            }
        }
        Context currContext = context;
        if(currContext == null) {
            currContext = mContext;
        }
        if(routeMeta == null || routeMeta.getUri() == null) {
            if(callback != null)
                callback.onLost(routeMeta);
            to404Page(currContext);
            return;
        }

        String uriStr = routeMeta.getUri().toString();
        if(uriStr.startsWith("http")) {
            //如果是http开头的协议，则用浏览器去打开
            Utils.launchBrowser(currContext, uriStr);
            if(callback != null)
                callback.onFound(routeMeta);
            return;
        }

        Intent intent;
        try {
            intent = new Intent(currContext, Class.forName(routeMeta.getRouteClass().activityClass));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            if(callback != null)
                callback.onLost(routeMeta);
            to404Page(currContext);
            return;
        }
        int flags = routeMeta.getFlags();
        if(!(currContext instanceof Activity)) {
            //如果不是Activity的实例， 必须加上FLAG_TASK的标记
            if(flags != -1) {
                flags |= Intent.FLAG_ACTIVITY_NEW_TASK;
            } else {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK;
            }
        }
        if(flags != -1) {
            //设置flag
            intent.setFlags(flags);
        }
        //设置传递参数
        intent.putExtras(routeMeta.getExtras());

        Uri uri = routeMeta.getUri();
        Map<String, String> paramMap = parseQueryParams(uri);
        Map<String, String> requiredParamMap = parseRequiredQueryParams(Uri.parse(routeMeta.getRouteClass().url));
        if(requiredParamMap != null && requiredParamMap.size() > 0) {
            for(Map.Entry<String, String> entry : requiredParamMap.entrySet()) {
                if((paramMap == null || TextUtils.isEmpty(paramMap.get(entry.getKey()))) &&
                        TextUtils.isEmpty(routeMeta.getExtras().getString(entry.getKey()))) {
                    //说明必填参数为空了
                    Utils.toast(currContext, currContext.getString(R.string.router_lack_params));
                    if(callback != null)
                        callback.onLost(routeMeta);
                    return;
                }
            }
        }
        //传递url里的参数
        if(paramMap != null && paramMap.size() > 0) {
            for(Map.Entry<String, String> entry : paramMap.entrySet()) {
                intent.putExtra(entry.getKey(), entry.getValue());
            }
        }

        if(currContext instanceof Activity) {
            Activity activity = (Activity) currContext;
            if(requestCode >= 0) {
                activity.startActivityForResult(intent, requestCode);
            } else {
                activity.startActivity(intent);
            }
            if(routeMeta.getEnterAnim() > 0 && routeMeta.getExitAnim() > 0) {
                activity.overridePendingTransition(routeMeta.getEnterAnim(), routeMeta.getExitAnim());
            }
        } else {
            currContext.startActivity(intent);
        }
        if(callback != null)
            callback.onFound(routeMeta);
    }

    /**
     * 解析出必填的查询参数
     *
     * @param uri
     * @return
     */
    private Map<String, String> parseRequiredQueryParams(Uri uri) {
        if(TextUtils.isEmpty(uri.getQuery())) {
           return null;
        }
        Set<String> keySet = uri.getQueryParameterNames();
        if(keySet != null && keySet.size() > 0) {
            Map<String, String> map = new HashMap<>();
            for(String key : keySet) {
                String value = uri.getQueryParameter(key);
                if(key.startsWith("*")) {
                    //有*说明是必填项
                    key = key.substring(1);
                    map.put(key, value);
                }
            }
            return map;
        }
        return null;
    }

    /**
     * 解析出查询参数
     *
     * @param uri
     * @return
     */
    private Map<String, String> parseQueryParams(Uri uri) {
        if(TextUtils.isEmpty(uri.getQuery())) {
            return null;
        }
        Set<String> keySet = uri.getQueryParameterNames();
        if(keySet != null && keySet.size() > 0) {
            Map<String, String> map = new HashMap<>();
            for(String key : keySet) {
                String value = uri.getQueryParameter(key);
                map.put(key, value);
            }
            return map;
        }
        return null;
    }

    /**
     * 页面不存在，默认跳转到404页面
     */
    private void to404Page(Context context) {
        Intent intent = new Intent(mContext, PageNotFoundActivity.class);
        if(!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

}
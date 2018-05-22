package com.hm.iou.router.entity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import com.hm.iou.router.NavigationCallback;
import com.hm.iou.router.Router;

import java.io.Serializable;

/**
 * Created by hjy on 17/12/12.<br>
 */

public class RouteInfo {

    /**
     * 构建一个空的路由信息对象
     *
     * @return
     */
    public static RouteInfo newEmptyInstance() {
        return new RouteInfo();
    }

    private Uri mUri;
    private RouteClassInfo mRouteClass;
    private int mFlags = -1;
    private Bundle mBundle;
    private int mEnterAnim;
    private int mExitAnim;

    private RouteInfo() {
        mBundle = new Bundle();
    }

    public RouteInfo(Uri uri, RouteClassInfo routeClass) {
        mBundle = new Bundle();
        mUri = uri;
        mRouteClass = routeClass;
    }

    public void setUri(Uri uri) {
        mUri = uri;
    }

    public void setRouteClassInfo(RouteClassInfo classInfo) {
        mRouteClass = classInfo;
    }

    public Uri getUri() {
        return mUri;
    }

    public RouteClassInfo getRouteClass() {
        return mRouteClass;
    }

    public int getEnterAnim() {
        return mEnterAnim;
    }

    public int getExitAnim() {
        return mExitAnim;
    }

    public int getFlags() {
        return mFlags;
    }

    public Bundle getExtras() {
        return mBundle;
    }

    public RouteInfo withFlags(int flags) {
        mFlags = flags;
        return this;
    }

    public RouteInfo withString(String key, String value) {
        mBundle.putString(key, value);
        return this;
    }

    public RouteInfo withBoolean(String key, boolean value) {
        mBundle.putBoolean(key, value);
        return this;
    }

    public RouteInfo withShort(String key, short value) {
        mBundle.putShort(key, value);
        return this;
    }

    public RouteInfo withInt(String key, int value) {
        mBundle.putInt(key, value);
        return this;
    }

    public RouteInfo withLong(String key, long value) {
        mBundle.putLong(key, value);
        return this;
    }

    public RouteInfo withDouble(String key, double value) {
        mBundle.putDouble(key, value);
        return this;
    }

    public RouteInfo withByte(String key, byte value) {
        mBundle.putByte(key, value);
        return this;
    }

    public RouteInfo withChar(String key, char value) {
        mBundle.putChar(key, value);
        return this;
    }

    public RouteInfo withFloat(String key, float value) {
        mBundle.putFloat(key, value);
        return this;
    }

    public RouteInfo withCharSequence(String key, CharSequence value) {
        mBundle.putCharSequence(key, value);
        return this;
    }

    public RouteInfo withParcelable(String key, Parcelable value) {
        mBundle.putParcelable(key, value);
        return this;
    }

    public RouteInfo withSerializable(String key, Serializable value) {
        mBundle.putSerializable(key, value);
        return this;
    }

    public RouteInfo withBundle(String key, Bundle value) {
        mBundle.putBundle(key, value);
        return this;
    }

    public RouteInfo withTransition(int enterAnim, int exitAnim) {
        mEnterAnim = enterAnim;
        mExitAnim = exitAnim;
        return this;
    }

    public void navigation() {
        navigation(null);
    }

    public void navigation(Context context) {
        navigation(context, -1);
    }

    public void navigation(Context context, int requestCode) {
        navigation(context, requestCode, null);
    }

    public void navigation(Context context, int requestCode, NavigationCallback callback) {
        Router.getInstance().navigation(context, this, requestCode, callback);
    }

}

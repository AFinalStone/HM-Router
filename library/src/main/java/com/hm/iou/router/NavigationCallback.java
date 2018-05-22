package com.hm.iou.router;


import com.hm.iou.router.entity.RouteInfo;

/**
 * Created by hjy on 17/12/12.<br>
 */

public interface NavigationCallback {

    void onFound(RouteInfo routeInfo);

    void onLost(RouteInfo routeInfo);

}

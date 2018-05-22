package com.hm.iou.router;


import com.hm.iou.router.entity.RouteInfo;

/**
 * Created by hjy on 17/12/12.<br>
 */

public interface RouteInterceptor {

    /**
     * 对路由进行拦截
     *
     * @param routeInfo
     * @return true表示对路由进行拦截，false则继续执行路由了
     */
    boolean onIntercept(RouteInfo routeInfo);

}

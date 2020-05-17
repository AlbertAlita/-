package com.taian.floatingballmatrix.Constant;

import com.taian.floatingballmatrix.base.BaseApplication;
import com.tamsiree.rxkit.RxDeviceTool;
import com.tamsiree.rxkit.RxImageTool;

/**
 * @Description:
 * @Author: baotaian
 * @Date: 2020/5/17
 * @Version:1.0
 */
public interface Constant {
    int i = (RxDeviceTool.getScreenWidth(BaseApplication.getInstance()) - RxImageTool.dip2px(28)) / 3;
}

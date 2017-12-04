package com.haha.mmxb.api;

import android.content.Context;

/**
 * Created by mmxb on 2017/12/4.
 */

public class MRouter {

    /**
     * 需要在Application中初始化
     *
     * @param context
     */
    public static void init(Context context) {
        PostRouter.getInstance().loadActivityMap(context);
    }

    /**
     * 构建导航器
     *
     * @param path 必须是唯一值，建议是当前Activity的名称
     * @return
     */
    public static PostRouter build(String path) {
        return PostRouter.getInstance().build(path);
    }
}
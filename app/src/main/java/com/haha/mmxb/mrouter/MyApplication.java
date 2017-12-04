package com.haha.mmxb.mrouter;

import android.app.Application;

import com.haha.mmxb.api.MRouter;

/**
 * Created by mmxb on 2017/12/4.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MRouter.init(this);
    }
}

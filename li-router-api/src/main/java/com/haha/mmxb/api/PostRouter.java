package com.haha.mmxb.api;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.haha.mmxb.annotation.Constant.ROUTER_CLASS_NAME;
import static com.haha.mmxb.annotation.Constant.ROUTER_PACKAGE_NAME;

/**
 * Created by mmxb on 2017/12/4.
 */


public class PostRouter {

    private static Map<String, Class> activityMap = new HashMap<>();
    private Context mContext;
    private Uri mUri;
    private Bundle mBundle;
    private int mFlags = -1;


    private static volatile PostRouter instance;

    protected static PostRouter getInstance() {
        if (instance == null) {
            synchronized (Class.class) {
                if (instance == null) {
                    instance = new PostRouter();
                }
            }
        }
        instance.reset();
        return instance;
    }

    /**
     * 清空上一次启动页面时传递的的Uri和Bundle
     */
    private void reset() {
        mUri = null;
        if (mBundle != null) {
            mBundle.clear();
            mBundle = null;
        }
        mBundle = new Bundle();
    }

    /**
     * 读取键值对，存到activityMap中
     */
    protected void loadActivityMap(Context context) {
        mContext = context;
        try {
            // 获取RouterProcessor中生成的导航器的类
            Class<?> mClass = Class.forName(ROUTER_PACKAGE_NAME + "." + ROUTER_CLASS_NAME);
            Constructor constructor = mClass.getConstructor();
            Loader routerLoader = (Loader) constructor.newInstance();
            // 调用loadActivityTable方法，将Router键值对存到activityMap中
            routerLoader.loadActivityMap(activityMap);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    public PostRouter build(String path) {
        mUri = TextUtils.isEmpty(path) ? null : Uri.parse(path);
        return this;
    }

    public PostRouter setFlags(int flags) {
        mFlags = flags;
        return this;
    }

    public PostRouter putExtra(String key, String value) {
        mBundle.putString(key, value);
        return this;
    }

    public PostRouter putExtra(String key, int value) {
        mBundle.putInt(key, value);
        return this;
    }

    public PostRouter putExtra(String key, float value) {
        mBundle.putFloat(key, value);
        return this;
    }

    public PostRouter putExtra(String key, double value) {
        mBundle.putDouble(key, value);
        return this;
    }

    public PostRouter putExtra(String key, boolean value) {
        mBundle.putBoolean(key, value);
        return this;
    }

    public PostRouter putExtra(String key, ArrayList<String> value) {
        mBundle.putStringArrayList(key, value);
        return this;
    }

    public PostRouter putExtra(String key, Serializable value) {
        mBundle.putSerializable(key, value);
        return this;
    }


    private Intent getIntent(Context context) {
        if (context == null || activityMap == null || activityMap.isEmpty()) {
            // todo log异常
            return null;
        }

        Intent intent = null;

        // 根据path，在activityMap中获取到要启动的Activity
        String path = mUri.getPath();
        Class targetActivity = activityMap.get(path);
        if (targetActivity == null) {
            // todo log异常
            return null;
        }

        intent = new Intent(context, targetActivity);

        // 添加bundle
        if (mBundle != null) {
            intent.putExtras(mBundle);
        }

        // 标志位
        if (mFlags > -1) {
            intent.setFlags(mFlags);
        }

        return intent;
    }

    public void start(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = getIntent(context);
        if (intent != null) {
            context.startActivity(intent);
        }
    }

    public void start(Fragment fragment) {
        if (fragment == null || fragment.getActivity() == null) {
            return;
        }

        Intent intent = getIntent(fragment.getActivity());
        if (intent != null) {
            fragment.startActivity(intent);
        }
    }

    public void start(Activity activity, int requestCode) {
        if (activity == null) {
            return;
        }
        Intent intent = getIntent(activity);
        if (intent != null) {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    public void start(Fragment fragment, int requestCode) {
        if (fragment == null || fragment.getActivity() == null) {
            return;
        }

        Intent intent = getIntent(fragment.getActivity());
        if (intent != null) {
            fragment.startActivityForResult(intent, requestCode);
        }
    }

}

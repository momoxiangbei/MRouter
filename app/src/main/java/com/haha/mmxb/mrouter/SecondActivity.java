package com.haha.mmxb.mrouter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.haha.mmxb.annotation.Route;

import static com.haha.mmxb.mrouter.SecondActivity.ROUTER_SECOND;

/**
 * Created by xueying on 2017/12/1.
 */

@Route(ROUTER_SECOND)
public class SecondActivity extends AppCompatActivity {

    public static final String ROUTER_SECOND = "SecondActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

    }

}

package com.haha.mmxb.mrouter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.haha.mmxb.api.MRouter;

import static com.haha.mmxb.mrouter.SecondActivity.ROUTER_SECOND;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startActivity(View view) {
        MRouter.build(ROUTER_SECOND).start(this);
    }
}

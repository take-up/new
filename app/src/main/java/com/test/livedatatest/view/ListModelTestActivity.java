package com.test.livedatatest.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.test.livedatatest.NameViewModel;

import org.w3c.dom.Text;

import www.baidutest.com.R;

public class ListModelTestActivity extends AppCompatActivity {

    private NameViewModel nameViewModel;

    private TextView tv_name;

    private Button bt_change;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_model_test);
        initView();

        nameViewModel = ViewModelProviders.of(this).get(NameViewModel.class);

        //创建观察者
        Observer<String> nameObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String name) {
                tv_name.setText(name);
            }
        };

        //观察LiveData 作为生命周期所有者和观察者传入此活动
        nameViewModel.getCurrentName().observe(this,nameObserver);
    }

    private void initView() {

        tv_name = (TextView) findViewById(R.id.tv_name);

        bt_change = (Button) findViewById(R.id.bt_change);

        bt_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameViewModel.getCurrentName().setValue("我是来自listData改变的数据");
            }
        });
    }

}

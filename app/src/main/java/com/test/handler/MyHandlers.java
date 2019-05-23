package com.test.handler;

import android.view.View;
import android.widget.Toast;

import www.baidutest.com.R;

public class MyHandlers {

    public void onClickFriend(View view) {

        int id = view.getId();

        if (id == R.id.tv_name) {
            Toast.makeText(view.getContext(), "点击了textView", Toast.LENGTH_LONG).show();
        }
    }
}

package hello.test.view;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * 使用databinding
 */
@SuppressLint("register")
public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createDatabingObj();
        initViewModelEvent();
    }

    protected abstract void createDatabingObj();

    private void initViewModelEvent() {

    }

}

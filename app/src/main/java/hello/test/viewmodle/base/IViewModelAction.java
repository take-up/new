package hello.test.viewmodle.base;

import android.arch.lifecycle.MutableLiveData;

import hello.test.event.BaseActionEvent;

public interface IViewModelAction {

    void startLoading();

    void startLoading(String message);

    void dismissLoading();

    void showToast(String message);

    void finish();

    void finishWithResultOk();

    MutableLiveData<BaseActionEvent> getActionData();
}

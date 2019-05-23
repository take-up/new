package hello.test.viewmodle.base;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import hello.test.event.BaseActionEvent;

public class BaseViewModel extends ViewModel implements IViewModelAction {

    private MutableLiveData<BaseActionEvent> actionLiveData;

    protected LifecycleOwner lifecycleOwner;

    public BaseViewModel() {
        actionLiveData = new MutableLiveData<>();
    }

    @Override
    public void startLoading() {
        startLoading(null);
    }

    @Override
    public void startLoading(String message) {
      BaseActionEvent baseActionEvent = new BaseActionEvent(BaseActionEvent.SHOW_LOADING_DIALOG);
      baseActionEvent.setMessage(message);
      actionLiveData.setValue(baseActionEvent);
    }

    @Override
    public void dismissLoading() {
        BaseActionEvent baseActionEvent = new BaseActionEvent(BaseActionEvent.DISMISS_LOADING_DIALOG);
        actionLiveData.setValue(baseActionEvent);
    }

    @Override
    public void showToast(String message) {
        BaseActionEvent baseActionEvent = new BaseActionEvent(BaseActionEvent.SHOW_TOAST);
        baseActionEvent.setMessage(message);
        actionLiveData.setValue(baseActionEvent);
    }

    @Override
    public void finish() {
        BaseActionEvent baseActionEvent = new BaseActionEvent(BaseActionEvent.FINISH);
        actionLiveData.setValue(baseActionEvent);
    }

    @Override
    public void finishWithResultOk() {
        BaseActionEvent baseActionEvent = new BaseActionEvent(BaseActionEvent.FINISH_WITH_RESULT_OK);
        actionLiveData.setValue(baseActionEvent);
    }

    @Override
    public MutableLiveData<BaseActionEvent> getActionData() {
        return actionLiveData;
    }

    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }
}

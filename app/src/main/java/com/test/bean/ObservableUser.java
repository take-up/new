package com.test.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;

import www.baidutest.com.BR;

/***
 * 双向绑定
 */
public class ObservableUser extends BaseObservable {

    private String firstName;
    private String lastName;

    private ObservableField<String> content = new ObservableField<>();

    public ObservableField<String> getContent() {
        return content;
    }

    @Bindable
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(BR.firstName);
    }

    @Bindable
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(BR.lastName);
    }
}

package edu.cnm.deepdive.homestead.viewmodel;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EventsViewModel extends ViewModel implements LifecycleObserver {

    private MutableLiveData<String> mText;

    public EventsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the events fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
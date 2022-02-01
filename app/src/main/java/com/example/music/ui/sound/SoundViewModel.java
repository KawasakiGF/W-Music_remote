package com.example.music.ui.sound;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SoundViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SoundViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is sound fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

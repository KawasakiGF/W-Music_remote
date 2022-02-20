package com.example.music.ui.sound;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.music.R;
import com.example.music.databinding.FragmentSoundBinding;

public class SoundViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SoundViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue(<<<曲名(title)を入れる>>>);
    }

    public LiveData<String> getText() {
        return mText;
    }
}

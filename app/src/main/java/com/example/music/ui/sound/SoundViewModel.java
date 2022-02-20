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
import io.realm.Realm;
import io.realm.RealmResults;
import com.example.music.MusicMetaDataModel;

public class SoundViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SoundViewModel(String path) {
        Realm realm=Realm.getDefaultInstance();
        RealmResults<MusicMetaDataModel> wantedMusic = realm.where(MusicMetaDataModel.class).equalTo("path",path).findAll();
        String title=wantedMusic.first().title;//pathは重複しない（1つのみ）ので、検索結果の一番目の(first)データのtitleを代入する
        mText = new MutableLiveData<>();
        mText.setValue(title);
    }

    public LiveData<String> getText() {
        return mText;
    }
}

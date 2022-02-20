package com.example.music.ui.sound;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.music.MusicMetaDataModel;

import io.realm.Realm;
import io.realm.RealmResults;

public class SoundViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SoundViewModel() {
        Realm realm=Realm.getDefaultInstance();
        RealmResults<MusicMetaDataModel> wantedMusic = realm.where(MusicMetaDataModel.class).findAll();
        String title=wantedMusic.first().title;//pathは重複しない（1つのみ）ので、検索結果の一番目の(first)データのtitleを代入する
        mText = new MutableLiveData<>();
        mText.setValue(title);
    }

    public LiveData<String> getText() {
        return mText;
    }
}

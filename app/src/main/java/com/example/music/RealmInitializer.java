package com.example.music;

import android.app.Application;
import io.realm.Realm;

public class RealmInitializer extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        Realm.init(this);
    }
}
/*
 このクラスはRealmの初期化用です
 こんな感じで定義したクラスをAndroidManifestのApplicationに登録しとくと
 アプリ起動時に初期化できるらしい
 MainActivityでやってもいいらしい わからんね でも動いてるよやったね
*/

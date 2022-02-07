package com.example.music;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


public class ModelUpdater extends Service {
    final static String TAG = "ModelUpdater";
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
//Log.d()はLogcatへログを流すのみ

/*
Todo
 1.モデルが無ければ作る処理(Realm.initで設定を変えてストレージに保存するようにするべきかも?)
 2.メタデータを追加する処理
 3.無ければ削除する処理
 4.Serviceとして適切に動作するように整える(非同期、別スレッド、セルフキル)
 */
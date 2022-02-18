package com.example.music;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.music.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;

import io.realm.Realm;

//importしろとは言われてなかったけど、動かないから多少はね？
//import com.example.music.MediaMetaRetriever;


public class MainActivity extends AppCompatActivity {

    Realm realm;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private static final int PERMISSION_WRITE_EX_STR = 1;

    @Override
    protected void onStop() {
        super.onStop();
//        if (transaction != null && !transaction.isCancelled()) {
//            transaction.cancel();
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        realm=Realm.getDefaultInstance();
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_artist, R.id.nav_sound)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        /* assets/musicFolder内のファイルをログ表示.*/
        displayAssets("musicFolder");

        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        //ストレージへの読み込み許可を取る
        if (android.os.Build.VERSION.SDK_INT >= 23 && permission != PackageManager.PERMISSION_GRANTED) {
            if (androidx.core.app.ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                androidx.core.app.ActivityCompat.requestPermissions(this,
                        new String[]{
                                android.Manifest.permission.READ_EXTERNAL_STORAGE
                        },
                        PERMISSION_WRITE_EX_STR);
            }
        }
        if (permission == PackageManager.PERMISSION_GRANTED) {
            //ここでストレージ読み込み処理を呼びたい
  //          ModelUpdater(realm);
        }


    }
/*
    @SuppressLint("Range")
    public void ModelUpdater(Realm realm) {





        final String TAG = "ModelUpdater";
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = null;
        StringBuilder sb = null;
        Log.d(TAG, "ModelUpdater wad called. ");

        try {
            cursor = contentResolver.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {*/

/*
                    //以下のコメントアウトは参考用に.append(str型のsbに文字を追加処理)のプログラムを残す
                    sb.append("ID: ");
                    sb.append(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                    Log.d("WATCH_METADATA", cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                    sb.append("\n");
                    sb.append("Title: ");
                    sb.append(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                    sb.append("\n");
                    sb.append("Path: ");
                    sb.append(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                    Log.d("WATCH_METADATA", cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                    sb.append("\n\n");
*/

/*
                } while (cursor.moveToNext());

                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

            Toast toast = Toast.makeText(this,
                    "例外が発生、Permissionを許可していますか？", Toast.LENGTH_SHORT);
            toast.show();

            //MainActivityに戻す
            finish();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                MusicMetaDataModel DataModel = bgRealm.createObject(MusicMetaDataModel.class);
                RealmResults<MusicMetaDataModel> finds= realm.where(MusicMetaDataModel.class).equalTo("path",cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))).findAll();
            } //todo cursorの受け渡しが無理っぽいのでArrayにデータを纏めてからRealm送りにするか‥
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // トランザクションは成功
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // トランザクションは失敗。自動的にキャンセルされます
            }
        });
    }
*/
    //許可が取れているかの確認
    @SuppressLint("Range")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permission, grantResults);
        Log.d("onRequestPermissionsResult", "onRequestPermissionsResult was called ");
        if (grantResults.length <= 0) {
            return;
        }
        switch (requestCode) {
            case PERMISSION_WRITE_EX_STR: {
                if (grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    /// 許可が取れた場合・・・
                    /// 必要な処理を書いておく
                } else {
                    /// 許可が取れなかった場合・・・
                    /// catchでこの処理を描いたので要らないかもしれん
                    android.widget.Toast.makeText(this,
                            "アプリを起動できません....", android.widget.Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void displayAssets(String dir) {
        AssetManager assetMgr = getResources().getAssets();
        try {
            String musicList[] = assetMgr.list(dir);
            for (int i = 0; i < musicList.length; i++) {
                Log.d("assets file", musicList[i]);
            }
        } catch (IOException e) {
        }
    }

}
package com.example.music;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmResults;

//importしろとは言われてなかったけど、動かないから多少はね？
//import com.example.music.MediaMetaRetriever;


public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_WRITE_EX_STR = 1;
    Realm realm;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        realm = Realm.getDefaultInstance();
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
/*

            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = null;
            StringBuilder sb = null;
            Log.d("ModelUpdater", "ModelUpdater wad called. ");

            try {
                cursor = contentResolver.query(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        null, null, null, null);

                String str = String.format(
                        "MediaStore.Audio = %s\n\n", cursor.getCount());
                Log.d("getcount","曲数: "+str);
                int aaa =Integer.parseInt(str);
                int [aaa][7] datalist;
                if (cursor != null && cursor.moveToFirst()) {
                    do {
*//*
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
*//*

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
            */
            List<MusicItem> Items = MusicItem.getItems(getApplicationContext());
            RealmAsyncTask transaction = realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    Log.d("Realm_ADD", "execute begin");
                    //                   MusicMetaDataModel DataModel = bgRealm.createObject(MusicMetaDataModel.class);

                    int i = 0;
                    for (i = 0; i < Items.size(); i++) {
                        RealmResults<MusicMetaDataModel> finds = bgRealm.where(MusicMetaDataModel.class).equalTo("path", Items.get(i).path).findAll();
                        if (finds.size() > 0) {//更新処理

                            finds.get(0).artist=Items.get(i).artist;
                            finds.get(0).truck=Items.get(i).truck;
                            finds.get(0).title=Items.get(i).title;
                            finds.get(0).album=Items.get(i).album;
                            finds.get(0).id=Items.get(i).id;
                            finds.get(0).duration=Items.get(i).duration;


                        } else {//追加処理

                            MusicMetaDataModel metadata = bgRealm.createObject(MusicMetaDataModel.class,Items.get(i).path);


                            metadata.artist=Items.get(i).artist;
                            metadata.truck=Items.get(i).truck;
                            metadata.title=Items.get(i).title;
                            metadata.album=Items.get(i).album;
                            metadata.id=Items.get(i).id;
                            metadata.duration=Items.get(i).duration;
                            bgRealm.copyFromRealm(metadata);

                        }
                    }
                    Log.d("Realm_ADD", String.valueOf(i)+" musics are collected");
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    // トランザクションは成功
                    Log.d("Realm_ADD", "onSuccess: ");
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    // トランザクションは失敗。自動的にキャンセルされます
                    Log.d("Realm_ADD", "onError: ");
                }
            });

        }


    }

    /*
        @SuppressLint("Range")
        public void ModelUpdater(Realm realm) {
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
                    List<MusicItem> Items = MusicItem.getItems(getApplicationContext());
                    RealmAsyncTask transaction = realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm bgRealm) {
                            Log.d("Realm_ADD", "execute begin");
                            //                   MusicMetaDataModel DataModel = bgRealm.createObject(MusicMetaDataModel.class);

                            int i = 0;
                            for (i = 0; i < Items.size(); i++) {

                                Log.d("Realm_ADD", "RealmResults begin");
                                RealmResults<MusicMetaDataModel> finds = bgRealm.where(MusicMetaDataModel.class).equalTo("path", Items.get(i).path).findAll();
                                Log.d("Realm_ADD", "RealmResults done");
                                if (finds.size() > 0) {//更新処理

                                    Log.d("Realm_ADD", "if");
                                    finds.get(0).artist=Items.get(i).artist;
                                    finds.get(0).truck=Items.get(i).truck;
                                    finds.get(0).title=Items.get(i).title;
                                    finds.get(0).album=Items.get(i).album;
                                    finds.get(0).id=Items.get(i).id;
                                    finds.get(0).duration=Items.get(i).duration;


                                    Log.d("Realm_ADD", "set Album-Duration done.");
                                } else {//追加処理

                                    MusicMetaDataModel metadata = bgRealm.createObject(MusicMetaDataModel.class,Items.get(i).path);


                                    metadata.artist=Items.get(i).artist;
                                    metadata.truck=Items.get(i).truck;
                                    metadata.title=Items.get(i).title;
                                    metadata.album=Items.get(i).album;
                                    metadata.id=Items.get(i).id;
                                    metadata.duration=Items.get(i).duration;
                                    bgRealm.copyFromRealm(metadata);

                                }
                            }
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            // トランザクションは成功
                            Log.d("Realm_ADD", "onSuccess: ");
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            // トランザクションは失敗。自動的にキャンセルされます
                            Log.d("Realm_ADD", "onError: ");
                        }
                    });

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
            String[] musicList = assetMgr.list(dir);
            for (int i = 0; i < musicList.length; i++) {
                Log.d("assets file", musicList[i]);
            }
        } catch (IOException e) {
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (transaction != null && !transaction.isCancelled()) {
//            transaction.cancel();
//        }
    }

    public static class MusicItem implements Comparable<Object> {
        private static final String TAG = "MusicItem";
        final long id;
        final String artist;
        final String title;
        final String album;
        final int truck;
        final long duration;
        final String path;

        public MusicItem(long id, String artist, String title, String album, int truck, long duration, String path) {
            this.id = id;
            this.artist = artist;
            this.title = title;
            this.album = album;
            this.truck = truck;
            this.duration = duration;
            this.path = path;
        }

        /**
         * 外部ストレージ上から音楽を探してリストを返す。
         *
         * @param context コンテキスト
         * @return 見つかった音楽のリスト
         */
        public static List<MusicItem> getItems(Context context) {
            List<MusicItem> items = new LinkedList<MusicItem>();

            // ContentResolver を取得
            ContentResolver cr = context.getContentResolver();

            // 外部ストレージから音楽を検索
            Cursor cur = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Audio.Media.IS_MUSIC + " = 1", null, null);

            if (cur != null) {
                if (cur.moveToFirst()) {
                    Log.i(TAG, "Listing...");

                    // 曲情報のカラムを取得
                    int artistColumn = cur.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                    int titleColumn = cur.getColumnIndex(MediaStore.Audio.Media.TITLE);
                    int albumColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM);
                    int durationColumn = cur.getColumnIndex(MediaStore.Audio.Media.DURATION);
                    int idColumn = cur.getColumnIndex(MediaStore.Audio.Media._ID);
                    int idTruck = cur.getColumnIndex(MediaStore.Audio.Media.TRACK);
                    int dataColumn = cur.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);

                    Log.i(TAG, "Title column index: " + titleColumn);
                    Log.i(TAG, "ID column index: " + titleColumn);

                    // リストに追加。>>item<< に曲の全てのデータが入ってる
                    do {
                        Log.i(TAG, "ID: " + cur.getString(idColumn) + " Title: " + cur.getString(titleColumn));
                        items.add(new MusicItem(cur.getLong(idColumn),
                                cur.getString(artistColumn),
                                cur.getString(titleColumn),
                                cur.getString(albumColumn),
                                cur.getInt(idTruck),
                                cur.getLong(durationColumn),
                                cur.getString(dataColumn)));
                    } while (cur.moveToNext());
                    Log.i(TAG, "Done querying media. MusicRetriever is ready.");
                }
                // カーソルを閉じる
                cur.close();
            }

            // 見つかる順番はソートされていないため、アルバム単位でソートする
            Collections.sort(items);
            return items;
        }

        public Uri getURI() {
            return ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
        }

        @Override
        public int compareTo(Object another) {
            if (another == null) {
                return 1;
            }

            MusicItem item = (MusicItem) another;
            int result = album.compareTo(item.album);

            if (result != 0) {
                return result;
            }

            return truck - item.truck;
        }

        public class ArtWork {
            public Bitmap getArtWork(String filePath) {

                Bitmap bm = null; //bmに画像ファイルが入る

                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(filePath);

                byte[] data = mmr.getEmbeddedPicture();

                // 画像が無ければnullになる
                if (null != data) {
                    bm = BitmapFactory.decodeByteArray(data, 0, data.length);
                }
                return bm;
            }
        }
    }
}
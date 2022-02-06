package com.example.music;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.music.databinding.ActivityMainBinding;
import java.io.IOException;
import android.app.Activity;
import android.content.res.AssetManager;
import android.util.Log;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//importしろとは言われてなかったけど、動かないから多少はね？
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
//import com.example.music.MediaMetaRetriever;



public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private static final int PERMISSION_WRITE_EX_STR = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        /// ストレージへの読み込み許可を取る
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if(androidx.core.app.ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != android.content.pm.PackageManager.PERMISSION_GRANTED)

            {
                androidx.core.app.ActivityCompat.requestPermissions(this,
                        new String[]{
                                android.Manifest.permission.READ_EXTERNAL_STORAGE
                        },
                        PERMISSION_WRITE_EX_STR);
            }
        }
    }

    //////////////////////////////////</松岡君のやってくれた方法以外で行けそうなの見つけたからそれのテスト
    public static class MusicItem implements Comparable<Object> {
        //Comparable<Object>の定義は結構大事っぽい->何がリストか分からん。items?
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

        public Uri getURI() {
            return ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
        }

        public class ArtWork {
            public Bitmap getArtWork(String filePath){

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

        /**
         * 外部ストレージ上から音楽を探してリストを返す。
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
                    int dataColmun = cur.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);

                    Log.i(TAG, "Title column index: " + String.valueOf(titleColumn));
                    Log.i(TAG, "ID column index: " + String.valueOf(titleColumn));

                    // リストに追加。>>item<< に曲の全てのデータが入ってる
                    do {
                        Log.i(TAG, "ID: " + cur.getString(idColumn) + " Title: " + cur.getString(titleColumn));
                        items.add(new MusicItem(cur.getLong(idColumn),
                                cur.getString(artistColumn),
                                cur.getString(titleColumn),
                                cur.getString(albumColumn),
                                cur.getInt(idTruck),
                                cur.getLong(durationColumn),
                                cur.getString(dataColmun)));
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
    }
    //////////////////////////////////松岡君のやってくれた方法以外で行けそうなの見つけたからそれのテスト/>

    @SuppressLint("Range")
    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permission, int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permission, grantResults);
        if (grantResults.length <= 0) {
            return;
        }
        switch (requestCode) {
            case PERMISSION_WRITE_EX_STR: {
                if (grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    /// 許可が取れた場合・・・
                    /// 必要な処理を書いておく
                    ContentResolver contentResolver = getContentResolver();
                    Cursor cursor = null;
                    StringBuilder sb = null;

                    try {
                        cursor = contentResolver.query(
                                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                null,null,null,null);

                        if (cursor != null && cursor.moveToFirst()) {
                            ///文字表示用やからこの処理はいらんけど、cursor.getCount()が総曲数をとれるみたいやね
                            String str =  String.format(
                                    "MediaStore.Audio = %s\n\n", cursor.getCount() );

                            sb = new StringBuilder(str);

                            List<Integer> list = new ArrayList<>();

                            do {
                                //この下がファイルにアクセスして楽曲のメタデータを取るところ
                                int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                                int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                                int albumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
                                int durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
                                int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
                                int idTruck = cursor.getColumnIndex(MediaStore.Audio.Media.TRACK);
                                int dataColmun = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);

                                list.add(artistColumn);
                                list.add(titleColumn);
                                list.add(albumColumn);
                                list.add(durationColumn);
                                list.add(idColumn);
                                list.add(idTruck);
                                list.add(dataColmun);

                                //以下のコメントアウトは参考用に.append(str型のsbに文字を追加処理)のプログラムを残す
                                sb.append("ID: ");
                                sb.append(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                                sb.append("\n");
                                sb.append("Title: ");
                                sb.append(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                                sb.append("\n");
                                sb.append("Path: ");
                                sb.append(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                                sb.append("\n\n");

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
                    } finally{
                        if(cursor != null){
                            cursor.close();
                        }
                    }

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

    private void displayAssets(String dir){
        AssetManager assetMgr = getResources().getAssets();
        try {
            String musicList[] = assetMgr.list(dir);
            for(int i = 0; i < musicList.length; i++) {
                Log.d("assets file", musicList[i]);
            }
        } catch (IOException e) {
        }
    }
}
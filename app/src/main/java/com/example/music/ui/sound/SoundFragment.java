package com.example.music.ui.sound;

import android.content.ContentUris;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.music.MusicMetaDataModel;
import com.example.music.R;
import com.example.music.databinding.FragmentSoundBinding;

import io.realm.Realm;
import io.realm.RealmResults;

public class SoundFragment extends Fragment {
//public class SoundFragment extends AppCompatActivity {
String TAG="SoundFragment_LOG";
    public MusicMetaDataModel metaData;
    public MediaPlayer mediaPlayer;
    public FragmentSoundBinding binding;
    public SeekBar seekBar;
    public Chronometer chronometer;
    public Chronometer durationMax;
    public ImageView button;
    public ImageView jacket;
    //public String path;
    Realm realm = Realm.getDefaultInstance();
    //RealmResults<MusicMetaDataModel> wantedMusic = realm.where(MusicMetaDataModel.class).equalTo("path",path).findAll();
    //long duration=wantedMusic.first().duration;
    RealmResults<MusicMetaDataModel> allList = realm.where(MusicMetaDataModel.class).findAll();
    String path = allList.first().path;
    Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,allList.first().id);
    long duration = allList.first().duration;
    private SoundViewModel soundViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sound, container, false);
        Log.d(TAG, "onCreateView: done");

        //曲名表示
        soundViewModel = new ViewModelProvider(this).get(SoundViewModel.class);
        binding = FragmentSoundBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSound;
        soundViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        ImageView button = (ImageView) view.findViewById(R.id.playButton);
        super.onCreate(savedInstanceState);
        //ここのRを通すために26行目にRのimportを入れてる。いや、下のは不要かも
        //setContentView(R.layout.fragment_sound);
        mediaPlayer = null;
        mediaPlayer = MediaPlayer.create(getContext(), uri);
        Log.d(TAG, "onCreateView: uri= "+uri);
        Log.d(TAG, "onCreateView: mediaPlayer init");
        //↑合ってると思うけど、曲名を入れてダメやったらあれやから下に元のプログラムを張っておく。
        //mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.sound);

        chronometer = view.findViewById(R.id.chronometer);
        durationMax = view.findViewById(R.id.durationMax);
        //曲の時間をdurationMaxに代入する。
        durationMax.setBase(duration);

        // SeekBar
        SeekBar seekBar = view.findViewById(R.id.seekBar);
        // 初期値
        seekBar.setProgress(0);
        // 最大値
        seekBar.setMax(100);

        //ジャケットの画像があれば割り当てる
        //ImageView jacket = (ImageView)view.findViewById(R.id.jacket);
        //jacket = <<<引っ張ってきたjacketをいれる>>>;
        Log.d(TAG, "onCreateView: end");
        return inflater.inflate(R.layout.fragment_sound, container, false);
    }

    public void onPlayButton(View v) {
        //表示内容の切り替え
        //ImageView button = (ImageView)findViewById(R.id.playButton);
        //再生中なら停止、停止中なら再生
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            Log.d(TAG, "onPlayButton: pause() called");
            button.setImageResource(R.drawable.start);
            //曲の進行時間をストップ
            chronometer.stop();
        } else {
            mediaPlayer.start();
            Log.d(TAG, "onPlayButton: start() called");
            button.setImageResource(R.drawable.pause);
            //曲の進行時間を表示
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
        }
    }

    //seekbarを離した時
    public void onStopTrackingTouch(SeekBar seekBar) {
        //seekのみ行う
        int msec = (int) duration * seekBar.getProgress() / seekBar.getMax();
        mediaPlayer.seekTo(msec);
        chronometer.setBase(SystemClock.elapsedRealtime() - mediaPlayer.getCurrentPosition());

    }

    //seekbarに触れたとき
    public void onStartTrackingTouch(SeekBar seekBar) {
        //すべて一時停止する
        chronometer.stop();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        button.setImageResource(R.drawable.pause);
    }

    //seekbarをドラッグしたとき
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Destroy");
        binding = null;
        mediaPlayer.release();
        mediaPlayer = null;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    // 音楽開始ボタン
    //Button buttonPlay = findViewById(R.id.play);
    //buttonPlay.setOnClickListener(v -> {
    //    audioPlay();
    //});

    // 音楽停止ボタン
    //Button buttonStop = findViewById(R.id.stop);

    //buttonStop.setOnClickListener( v -> {
    //    if (mediaPlayer != null) {
    //        audioStop();
    //    }
    //});
    //}
    //private void audioPlay(){
    //if(mediaPlayer == null){
    //audioファイルを読み出し
    //}
    //else{
    // 繰り返し再生する場合
    //    mediaPlayer.stop();
    //    mediaPlayer.reset();
    // リソースの解放
    //    mediaPlayer.release();
    //}
    // 再生する
    //mediaPlayer.start();
    //}

    //private void audioStop() {
    // 再生終了
    //mediaPlayer.stop();
    // リセット
    //mediaPlayer.reset();
    // リソースの解放
    //mediaPlayer.release();

    //mediaPlayer = null;
    //}


    //public View onCreateView(@NonNull LayoutInflater inflater,
    //                         ViewGroup container, Bundle savedInstanceState) {
    //setContentView(R.layout.fragment_sound);
    //soundViewModel =
    //        new ViewModelProvider(this).get(SoundViewModel.class);


    //AssetManager assetMgr = getResources().getAssets();
    //try {
    //    String[] musicList = assetMgr.list("musicFolder");
    //} catch (IOException e) {
    //}

    //binding = FragmentSoundBinding.inflate(inflater, container, false);
    //View root = binding.getRoot();


    //final TextView textView = binding.textSound;
    //soundViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
    //@Override
    //public void onChanged(@Nullable String s) {
    //textView.setText(s);


    //}
    //});
    //return root;
    //}

}


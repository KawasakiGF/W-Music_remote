package com.example.music.ui.sound;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.music.R;
import com.example.music.databinding.FragmentSoundBinding;

import java.io.IOException;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Button;
import android.content.res.AssetFileDescriptor;
import android.widget.Toast;
import android.content.Intent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SoundFragment  extends Fragment {

    private MediaPlayer mediaPlayer;
    private SoundViewModel soundViewModel;
    private FragmentSoundBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        soundViewModel =
                new ViewModelProvider(this).get(SoundViewModel.class);

        AssetManager assetMgr = getResources().getAssets();
        try {
            String[] musicList = assetMgr.list("musicFolder");
        } catch (IOException e) {
        }

        binding = FragmentSoundBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        //final TextView textView = binding.textSound;
        //soundViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            //@Override
            //public void onChanged(@Nullable String s) {
                //textView.setText(s);


            //}
        //});
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


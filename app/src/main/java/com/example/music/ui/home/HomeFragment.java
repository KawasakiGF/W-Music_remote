package com.example.music.ui.home;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.music.R;
import com.example.music.databinding.FragmentHomeBinding;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetManager;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

//public class MyContext {
    //private static MyContext instance = null;
    //private Context applicationContext;
    //// publicをつけないのは意図的
    //// MyApplicationと同じパッケージにして、このメソッドのアクセスレベルはパッケージローカルとする
    //// 念のため意図しないところで呼び出されることを防ぐため
    //static void onCreateApplication(Context applicationContext) {
    //    // Application#onCreateのタイミングでシングルトンが生成される
    //    instance = new MyContext(applicationContext);
    //}
    //private void MyContext(Context applicationContext) {
    //    this.applicationContext = applicationContext;
    //} public static MyContext getInstance() {
    //    if (instance == null) {
    //        // こんなことは起きないはず
    //        throw new RuntimeException("MyContext should be initialized!");
    //    }
    //    return instance;
    //}
//}


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        AssetManager assetMgr = getResources().getAssets();
        try {
            String[] musicList = assetMgr.list("musicFolder");
        } catch (IOException e) {
        }

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
                //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                //        getApplicationContext(),
                //        android.R.layout.simple_list_item_1,    // Androidに組み込まれているレイアウト
                //        musicList
                //);

                //ListView listView = findViewById(R.id.music_list);

                //listView.setAdapter(arrayAdapter);
            }
        });
        return root;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


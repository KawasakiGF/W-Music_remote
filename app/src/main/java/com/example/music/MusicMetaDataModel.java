package com.example.music;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MusicMetaDataModel extends RealmObject {

    public long id;
    public String artist;
    public String title;
    public String album;
    public int truck;
    public long duration;

    @PrimaryKey
    public String path;


    }

/*
楽曲のメタデータを保存するためのモデルを定義
MainActivity.java内のpublic MusicItem()の定義を参考にしてidからpathまで受け取れるようにしてある
 */
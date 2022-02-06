package com.example.music;

import io.realm.RealmObject;

public class MusicMetaDataModel extends RealmObject {

    private long id=0;
    private String artist=null;
    private String title=null;
    private String album=null;
    private int truck=0;
    private long duration=0;
    private String path=null;
}
/*
楽曲のメタデータを保存するためのモデルを定義
MainActivity.java内のpublic MusicItem()の定義を参考にしてidからpathまで受け取れるようにしてある
 */
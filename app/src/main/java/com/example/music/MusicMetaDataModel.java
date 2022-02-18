package com.example.music;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MusicMetaDataModel extends RealmObject {

    private long id;
    private String artist;
    private String title;
    private String album;
    private int truck;
    private long duration;

    @PrimaryKey
    private String path;

    public void setPath(String path) {
        this.path = path;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setTruck(int truck) {this.truck = truck;}

    public void setDuration(long duration) {this.duration = duration;}

    public long getId(){return id;}
    public String getArtist(){return artist;}
    public String  getTitle(){return title;}
    public String  getAlbum(){return album;}
    public int getTruck(){return truck;}
    public long getDuration(){return duration;}
    public String getPath(){return path;
    }
}
/*
楽曲のメタデータを保存するためのモデルを定義
MainActivity.java内のpublic MusicItem()の定義を参考にしてidからpathまで受け取れるようにしてある
 */
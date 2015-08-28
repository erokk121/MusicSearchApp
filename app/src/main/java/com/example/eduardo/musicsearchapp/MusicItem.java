package com.example.eduardo.musicsearchapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eduardo on 8/27/2015.
 */
public class MusicItem implements Parcelable {

    String track, artist, album, imageUrl;

    public MusicItem(Parcel in) {
        this.track = in.readString();
        this.artist = in.readString();
        this.album = in.readString();
        this.imageUrl = in.readString();
    }

    public MusicItem(String track, String artist, String album, String imageUrl) {
        this.track = track;
        this.artist = artist;
        this.album = album;
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.track);
        dest.writeString(this.artist);
        dest.writeString(this.album);
        dest.writeString(this.imageUrl);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MusicItem createFromParcel(Parcel in) {
            return new MusicItem(in);
        }

        public MusicItem[] newArray(int size) {
            return new MusicItem[size];
        }
    };

}

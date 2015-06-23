package com.sickofyou.spotifystreamerpart1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dtorres on 06/22/2015.
 */
public class SpotifyArtist implements Parcelable {
    public static final Creator<SpotifyArtist> CREATOR = new Creator<SpotifyArtist>() {
        @Override
        public SpotifyArtist createFromParcel(Parcel in) {
            return new SpotifyArtist(in);
        }

        @Override
        public SpotifyArtist[] newArray(int size) {
            return new SpotifyArtist[size];
        }
    };
    private String name;
    private String imageUrl;
    private String id;

    public SpotifyArtist(String name, String imageUrl, String id) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.id = id;
    }

    private SpotifyArtist(Parcel in) {
        name = in.readString();
        imageUrl = in.readString();
        id = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imageUrl);
        dest.writeString(id);
    }

    public String getName() {
        return name;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getId() {
        return id;
    }
}

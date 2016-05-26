package com.ferr3t.don.gurumovies;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Don on 1/28/2016.
 */
public class Movie implements Parcelable {

    String title;
    String description;
    String voteAverage;
    String numOfVotes;
    String posterPath;
    String backDrop;

    public Movie(){}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(voteAverage);
        dest.writeString(numOfVotes);
        dest.writeString(posterPath);
        dest.writeString(backDrop);
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie(Parcel in) {
        title = in.readString();
        description = in.readString();
        voteAverage = in.readString();
        numOfVotes = in.readString();
        posterPath = in.readString();
        backDrop = in.readString();

    }
}



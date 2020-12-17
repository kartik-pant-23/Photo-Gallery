package com.example.gallerysaver;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Image implements Parcelable {

    String imgUri;
    boolean checked;

    public Image(@NonNull String imgUri) {
        this.imgUri =imgUri;
    }

    protected Image(Parcel in) {
        imgUri = in.readString();
        checked = in.readByte() != 0;
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public void setChecked(boolean checked) {
        this.checked=checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public String getImgUri() {
        return imgUri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imgUri);
        parcel.writeInt(checked ?1 :0);
    }
}

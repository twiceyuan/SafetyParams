package com.twiceyuan.activityargs.sample.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableBean implements Parcelable {

    public ParcelableBean(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public static final Parcelable.Creator<ParcelableBean> CREATOR = new Parcelable.Creator<ParcelableBean>() {
        @Override
        public ParcelableBean createFromParcel(Parcel source) {
            return new ParcelableBean(source);
        }

        @Override
        public ParcelableBean[] newArray(int size) {
            return new ParcelableBean[size];
        }
    };

    private String name;
    private int    number;

    public ParcelableBean() {
    }

    protected ParcelableBean(Parcel in) {
        this.name = in.readString();
        this.number = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.number);
    }
}

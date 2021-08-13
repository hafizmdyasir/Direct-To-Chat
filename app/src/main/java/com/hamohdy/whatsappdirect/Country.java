package com.hamohdy.whatsappdirect;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * POJO to represent a country*/
public class Country implements Parcelable {

    public static final Creator<Country> CREATOR = new Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel in) {
            return new Country(in);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };

    public final String name;
    public final String isoCode;
    public final String isdCode;

    //Note that this represent a resource id of the form R.mipmap.xx
    public final int flagCode;


    public Country(String name, String isoCode, String isdCode, int flagCode) {
        this.name = name;
        this.isoCode = isoCode;
        this.isdCode = isdCode;
        this.flagCode = flagCode;
    }

    protected Country(Parcel in) {
        this.name = in.readString();
        this.isoCode = in.readString();
        this.isdCode = in.readString();
        this.flagCode = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(isoCode);
        dest.writeString(isdCode);
        dest.writeInt(flagCode);
    }
}

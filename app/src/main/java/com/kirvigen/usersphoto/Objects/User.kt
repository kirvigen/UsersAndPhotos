package com.kirvigen.usersphoto.Objects

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class User(
    val name:String,
    val id:Int,
    val albums:MutableList<Album>
    ):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.createTypedArrayList(Album.CREATOR) as MutableList<Album>
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(id)
        parcel.writeTypedList(albums)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

}
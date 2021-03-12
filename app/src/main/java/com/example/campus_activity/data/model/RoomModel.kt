package com.example.campus_activity.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import java.util.ArrayList


data class RoomModel(
    var id: String?,
    var name: String?,
    var admin:String?,
    var members: ArrayList<String>?,
    var lastMessage: String?,
    var timestamp: Timestamp?
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.readParcelable(Timestamp::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(admin)
        parcel.writeStringList(members)
        parcel.writeString(lastMessage)
        parcel.writeParcelable(timestamp, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RoomModel> {
        override fun createFromParcel(parcel: Parcel): RoomModel {
            return RoomModel(parcel)
        }

        override fun newArray(size: Int): Array<RoomModel?> {
            return arrayOfNulls(size)
        }
    }
}
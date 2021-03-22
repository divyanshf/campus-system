package com.example.campus_activity.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import java.util.*


data class RoomModel(
    var id: String?,
    var name: String?,
    var admin:String?,
    var members: ArrayList<String>?,
    var timestamp: Timestamp?,
    var uri: String?
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readParcelable(Timestamp::class.java.classLoader),
        parcel.readString(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(admin)
        parcel.writeStringList(members)
        parcel.writeParcelable(timestamp, flags)
        parcel.writeString(uri)
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
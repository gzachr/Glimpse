package com.mobdeve.s12.group8.glimpse.model

import android.os.Parcel
import android.os.Parcelable

class Reaction(
    var postImageId: Int,
    var username: String,
    var reactionDate: String,
    var position: Int
) : Parcelable {

    private constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(postImageId)
        parcel.writeString(username)
        parcel.writeString(reactionDate)
        parcel.writeInt(position)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Reaction> {
        override fun createFromParcel(parcel: Parcel): Reaction {
            return Reaction(parcel)
        }

        override fun newArray(size: Int): Array<Reaction?> {
            return arrayOfNulls(size)
        }
    }
}

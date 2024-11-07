import android.os.Parcel
import android.os.Parcelable

data class OldPost(
    var postImageId: Int,
    var userImageId: Int,
    var username: String,
    var createdAt: String,
    var caption: String,
    var position: Int
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(postImageId)
        parcel.writeInt(userImageId)
        parcel.writeString(username)
        parcel.writeString(createdAt)
        parcel.writeString(caption)
        parcel.writeInt(position)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OldPost> {
        override fun createFromParcel(parcel: Parcel): OldPost {
            return OldPost(parcel)
        }

        override fun newArray(size: Int): Array<OldPost?> {
            return arrayOfNulls(size)
        }
    }
}

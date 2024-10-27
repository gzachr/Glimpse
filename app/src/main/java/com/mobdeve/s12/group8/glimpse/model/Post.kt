import android.os.Parcel
import android.os.Parcelable

data class Post(
    var postImageId: Int,
    var userImageId: Int,
    var username: String,
    var createdAt: String,
    var caption: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(postImageId)
        parcel.writeInt(userImageId)
        parcel.writeString(username)
        parcel.writeString(createdAt)
        parcel.writeString(caption)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Post> {
        override fun createFromParcel(parcel: Parcel): Post {
            return Post(parcel)
        }

        override fun newArray(size: Int): Array<Post?> {
            return arrayOfNulls(size)
        }
    }
}

package com.example.githubusernew.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Favorite (
    var id: Int = 0,
    var login: String? = null,
    var avatarUrl: String? = null,
    var type : String? = null,
    var favorite: Int = 0
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    companion object : Parceler<Favorite> {

        override fun Favorite.write(parcel: Parcel, flags: Int) {
            parcel.writeInt(id)
            parcel.writeString(login)
            parcel.writeString(avatarUrl)
            parcel.writeString(type)
            parcel.writeInt(favorite)
        }

        override fun create(parcel: Parcel): Favorite {
            return Favorite(parcel)
        }
    }
}
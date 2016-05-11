package com.codefactory.przemyslawdabrowski.nearinpost.model.ui

import android.os.Parcel
import android.os.Parcelable
import com.codefactory.przemyslawdabrowski.nearinpost.view.main.item.ItemData

/**
 * Postal code, result of search for location action.
 */
data class PostalCodeUi(val postalCode: String) : ItemData, Parcelable {
    constructor(source: Parcel): this(source.readString())

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(postalCode)
    }

    companion object {
        @JvmField final val CREATOR: Parcelable.Creator<PostalCodeUi> = object : Parcelable.Creator<PostalCodeUi> {
            override fun createFromParcel(source: Parcel): PostalCodeUi {
                return PostalCodeUi(source)
            }

            override fun newArray(size: Int): Array<PostalCodeUi?> {
                return arrayOfNulls(size)
            }
        }
    }
}
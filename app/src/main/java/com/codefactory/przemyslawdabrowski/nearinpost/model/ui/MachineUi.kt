package com.codefactory.przemyslawdabrowski.nearinpost.model.ui

import android.os.Parcel
import android.os.Parcelable
import com.codefactory.przemyslawdabrowski.nearinpost.model.api.Machine
import com.codefactory.przemyslawdabrowski.nearinpost.view.main.item.ItemData

data class MachineUi(val name: String?
                     , val postcode: String?
                     , val street: String?
                     , val buildingNumber: String?
                     , val town: String?
                     , val latitude: Float?
                     , val longitude: Float?
                     , val distance: Double?
                     , val locationDescription: String?) : Parcelable, ItemData {

    /**
     * Secondary constructor.
     * @param machine Machine to create MachineUi object.
     */
    constructor(machine: Machine) : this(machine.name
            , machine.postcode
            , machine.street
            , machine.buildingnumber
            , machine.town
            , machine.latitude
            , machine.longitude
            , machine.distance
            , machine.locationdescription)

    constructor(source: Parcel) : this(source.readSerializable() as String?, source.readSerializable() as String?, source.readSerializable() as String?, source.readSerializable() as String?, source.readSerializable() as String?, source.readSerializable() as Float?, source.readSerializable() as Float?, source.readSerializable() as Double?, source.readSerializable() as String?)

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeSerializable(name)
        dest?.writeSerializable(postcode)
        dest?.writeSerializable(street)
        dest?.writeSerializable(buildingNumber)
        dest?.writeSerializable(town)
        dest?.writeSerializable(latitude)
        dest?.writeSerializable(longitude)
        dest?.writeSerializable(distance)
        dest?.writeSerializable(locationDescription)
    }

    companion object {
        @JvmField final val CREATOR: Parcelable.Creator<MachineUi> = object : Parcelable.Creator<MachineUi> {
            override fun createFromParcel(source: Parcel): MachineUi {
                return MachineUi(source)
            }

            override fun newArray(size: Int): Array<MachineUi?> {
                return arrayOfNulls(size)
            }
        }
    }
}
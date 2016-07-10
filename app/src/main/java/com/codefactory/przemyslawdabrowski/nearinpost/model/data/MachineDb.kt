package com.codefactory.przemyslawdabrowski.nearinpost.model.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

//If members declared in constructor, they need to have default values!!! Realm <3
open class MachineDb(@PrimaryKey open var name: String = "",
                     open var postcode: String? = null,
                     open var street: String? = null,
                     open var buildingNumber: String? = null,
                     open var town: String? = null,
                     open var latitude: Float? = null,
                     open var longitude: Float? = null,
                     open var distance: Double? = null,
                     open var locationDescription: String? = null,
                     open var searchPostalCode: String = "") : RealmObject() {}
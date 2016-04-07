package com.codefactory.przemyslawdabrowski.nearinpost.model.api

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "paczkomaty", strict = false)
class Paczkomaty() {

    /**
     * List of nearest from REST request postcode inPost machines.
     */
    @field:ElementList(name = "machines", entry = "machine", inline = true, required = false)
    var machine: List<Machine> = emptyList()
}



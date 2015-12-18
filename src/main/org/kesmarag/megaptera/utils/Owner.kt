package org.kesmarag.megaptera.utils

open class Owner(private val id: Int = 0) {
    public fun isOwned(property: Property): Boolean{
        return property.ownerID == id
    }
}
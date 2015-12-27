package org.kesmarag.megaptera.utils

import java.util.*

open class Property {
    private var freedom: Boolean = true
    public var ownerID: Int = -1
        private set

    public fun set(owner: Int) {
        if (freedom) {
            ownerID = owner
            freedom = false
        } else {
            println("This class has already got an owner, nothing to do")
        }
    }

    constructor() {
        ownerID = -1
        freedom = true
    }

    constructor(owner: Int) {
        ownerID = owner
        freedom = false
    }

    constructor(candidateOwners: Int, startID: Int) {
        if (candidateOwners > 1) {
            val rand: Random = Random()
            ownerID = rand.nextInt(candidateOwners) + startID
        } else {
            ownerID = startID
        }
        freedom = false
    }

    public fun redistribute(candidateOwners: Int, startID: Int) {
        val rand: Random = Random()
        ownerID = rand.nextInt(candidateOwners) + startID
        freedom = false
    }

    public fun free() {
        ownerID = -1
        freedom = true
    }

    public fun freeToChange() {
        freedom = true
    }


    public fun forceSetOwner(owner: Int) {
        ownerID = owner
        freedom = false
    }

    public fun isFree() = freedom
}
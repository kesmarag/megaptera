package org.kesmarag.megaptera.hmm

import org.kesmarag.megaptera.utils.DataSet
import java.util.*

class GhmmClustering(val dataSet: DataSet, val clusters: Int = 3, val states: Int = 3, val mixtures: Int = 2) {
    private var changes: Int = dataSet.size
    private var minChanges: Int = dataSet.size
    private var minChangesRepetions: Int = 0
    public var ghmm: MutableList<GaussianHiddenMarkovModel> = arrayListOf()
        private set

    init {
        dataSet.members.forEach { it.redistribute(1, -1); it.freeToChange() }
        dataSet.members.forEach { it.data.forEach { it.redistribute(1, -1) ; it.freeToChange() }; it.scores = DoubleArray(clusters) }
        val rand: Random = Random()
        if (dataSet.size < clusters) {
            throw IllegalArgumentException("DataSet size must be bigger than the number of clusters")
        }
        for (k in 0..clusters - 1) {
            var i = rand.nextInt(dataSet.size)
            println("i = $i")
            while (!dataSet[i].isFree()) {
                i = rand.nextInt(dataSet.size)
                println("i = $i")
            }
            dataSet[i].forceSetOwner(k)
        }
        // creating clusters
        for (k in 0..clusters - 1) {
            ghmm.add(GaussianHiddenMarkovModel(dataSet, states, mixtures, k))
        }
        for (k in 0..clusters - 1) {
            println("##### k = $k #####")
            ghmm[k].update()
        }
    }

    private fun calculateScores(): Unit {
        dataSet.members.forEach {
            for (k in 0..clusters - 1) {
                it.scores[k] = ghmm[k].posterior(it)
            }
        }
    }

    private fun updateOwnerships() {
        changes = 0
        calculateScores()
        dataSet.members.forEach {
            var j: Int = 0
            var max: Double = it.scores[0]
            for (k in 1..clusters - 1) {
                if (it.scores[k] > max ) {
                    max = it.scores[k]
                    j = k
                }
            }
            if (j != it.ownerID) {
                it.forceSetOwner(j)
                changes++
            }
        }
        for (k in 0..clusters - 1) {
            ghmm[k].update()
        }
        println("changes = $changes")
    }

    public fun training() {
        do {
            if (changes < minChanges) {
                minChanges = changes
                minChangesRepetions = 1
            } else if (changes == minChanges) {
                minChangesRepetions++
            }
            updateOwnerships()
        } while (changes > 0 && minChangesRepetions <= 3)
    }

}
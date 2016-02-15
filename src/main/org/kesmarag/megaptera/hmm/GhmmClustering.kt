package org.kesmarag.megaptera.hmm

import org.kesmarag.megaptera.utils.DataSet
import java.io.Serializable
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class GhmmClustering(val dataSet: DataSet, val clusters: Int = 3, val states: Int = 3, val mixtures: Int = 2)
: Serializable {
    private var changes: Int = dataSet.size
    private var minChanges: Int = dataSet.size
    private var minChangesRepetions: Int = 0
    public val cores = Math.min(Runtime.getRuntime().availableProcessors(), 8)
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
            println("##################")
        }
    }

    private fun calculateScores(): Unit {
        dataSet.members.forEach {
            for (k in 0..clusters - 1) {
                it.scores[k] = ghmm[k].posterior(it)
            }
        }

        dataSet.members.forEach {
            val min_score: Double = it.scores.min()?.toDouble() ?: 0.0
            val max_score: Double = it.scores.max()?.toDouble() ?: 0.0
            for (k in 0..clusters - 1) {
                it.scores[k] = (it.scores[k] - min_score) / (max_score - min_score)
            }
        }

        /*
        var mean = DoubleArray(clusters)
        dataSet.members.forEach {
            for (k in 0..clusters-1){
                mean[k] += it.scores[k]
            }
        }
        var varianceN = DoubleArray(clusters)
        dataSet.members.forEach {
            for (k in 0..clusters-1){
                varianceN[k] += (it.scores[k]-mean[k]/dataSet.size.toDouble())*(it.scores[k]-mean[k]/dataSet.size.toDouble())
            }
        }
        dataSet.members.forEach {
            for (k in 0..clusters-1){
                it.scores[k] = (it.scores[k]-mean[k]/dataSet.size.toDouble())/Math.sqrt(varianceN[k]/dataSet.size.toDouble())
            }
        }
        */
    }

    private fun updateOwnerships() {

        changes = 0
        calculateScores()
        dataSet.members.forEach {
            var j = 0
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
        var threadPool: ExecutorService = Executors.newFixedThreadPool(cores)
        for (k in 0..clusters - 1) {
            threadPool.execute(Runnable { ghmm[k].update() })
        }
        threadPool.shutdown();
        threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);


        println("changes = $changes")
    }

    private fun updateOwnerships2() {
        val perCluster: Int = dataSet.members.size / clusters
        changes = 0
        calculateScores()
        dataSet.members.forEach {
            var j = 0
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
            } else {

            }
        }
        charityWork()
        var threadPool: ExecutorService = Executors.newFixedThreadPool(cores)
        for (k in 0..clusters - 1) {
            threadPool.execute(Runnable { ghmm[k].update() })
        }
        threadPool.shutdown();
        threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);


        println("changes = $changes")
    }

    private fun charityWork() {
        println("charity started...")
        val probability: Double = 0.01
        val perCluster: Int = dataSet.members.size / clusters
        val popularity: IntArray = IntArray(clusters)
        for (k in 0..clusters - 1) {
            popularity[k] = ghmm[k].members.size - perCluster
        }
        ghmm.filterIndexed { i, g -> popularity[i] > perCluster }
                .filter { Random().nextDouble() > probability }
                .flatMap { it.members }
                .map { dataSet.members[it] }
                .forEach { it.forceSetOwner(Random().nextInt(clusters)); changes++ ;println(":Charity:") }

        println("charity completed...")
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
package org.kesmarag.megaptera.kmeans

import org.kesmarag.megaptera.utils.DataSet
import org.kesmarag.megaptera.utils.Observation
import org.kesmarag.megaptera.utils.Owner
import java.io.Serializable

class Kmeans(val dataSet: DataSet, val clusters:Int = 3, id: Int = 0): Owner(id), Serializable {
    public var centroids: Array<DoubleArray> = Array(clusters, { DoubleArray(dataSet.observationLength) })
        private set
    private var observationList: List<Observation> = dataSet.members
            .filter { isOwned(it) }
            .flatMap { it.data.toList() }
    private var changes = 0
    init {
        adapt()
        display()
    }

    public fun adapt(){
        observationList.forEach { it.redistribute(clusters,0) }
        var step: Int = 0
        do{
            step++
            var sum = Array(clusters,{ DoubleArray(dataSet.observationLength)})
            var L = IntArray(clusters)
            changes = 0
            observationList.forEach {
                L[it.ownerID]++
                for (n in 0..dataSet.observationLength-1){
                    centroids[it.ownerID][n] += it.data[n]
                }
            }

            centroids.forEachIndexed { i, it->
                for (n in 0..dataSet.observationLength-1){
                    centroids[i][n] /= (L[i].toDouble()+1)
                }
            }

            for (i in 0..observationList.size-1){
                var pos: Int = 0
                var min: Double = dist(observationList[i].data, centroids[0])
                for (k in 1..clusters-1){
                    if (dist(observationList[i].data,centroids[k]) < min){
                        min = dist(observationList[i].data, centroids[k])
                        pos = k
                    }
                }
                if (observationList[i].ownerID !=  pos ){
                    changes++
                    observationList[i].forceSetOwner(pos)
                }
                for (n in 0..dataSet.observationLength-1){
                    sum[pos][n] += observationList[i].data[n]
                }
                L[pos]++
            }
        }while(changes > 0 && step<=100)

    }

    public fun dist(a1: DoubleArray,a2: DoubleArray): Double{
        var s: Double = 0.0
        for (n in 0..a1.size-1){
            s += (a1[n]-a2[n])*(a1[n]-a2[n])
        }
        return s
    }

    public fun display(): Unit{
        println("==> centroids <==")
        centroids.forEach {
            for (n in 0..dataSet.observationLength-1){
                print(it[n])
                print(" ")
            }
            println()
        }
    }
}
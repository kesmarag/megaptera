package org.kesmarag.megaptera.scripts

import org.kesmarag.megaptera.distributions.GaussianMixtureDensity
import org.kesmarag.megaptera.hmm.GaussianHiddenMarkovModel
import org.kesmarag.megaptera.hmm.GhmmClustering
import org.kesmarag.megaptera.utils.DataSet
import org.kesmarag.megaptera.utils.Observation
import org.kesmarag.megaptera.utils.ObservationSet

fun main(args: Array<String>) {
    println(".:: megaptera v0.1 ::.")
    //val pat: String? = readLine()
    val dataSet = DataSet("/home/kesmarag/tmp/example2/test*",1)
    //val dataSet = DataSet("/home/kesmarag/tmp/example/faith*",1)
    dataSet.members.forEach { it.standardize() }
  //  dataSet.members.forEach { println(it[0][0]) }
   // val dataSet = DataSet(pat?:"*.csv",1)

    val hmm = GaussianHiddenMarkovModel(dataSet, 4, 6 , 0)


    hmm.update()
   // println(Math.exp(Math.log(0.0)).isNaN())
    //print(0.0/0.0)
    // val clustering = GhmmClustering(dataSet,10,3,1)
    //clustering.training()
    //val w = doubleArrayOf(0.1,0.9)
    //val m = arrayOf(doubleArrayOf(0.0,0.0),doubleArrayOf(0.0,0.0))
    //val s = arrayOf(doubleArrayOf(1.0,1.0),doubleArrayOf(1.0,1.0))
    //val gmm = GaussianMixtureDensity2(w,m,s)
    //val gmm_k = GaussianMixtureDensity(w,m,s)
   // val x = doubleArrayOf(100.0,100.0)
   // val o = Observation(x)
    //println(gmm.mixtureContribution(o,0))
    //println(gmm_k.mixtureContribution(o,1))

    //println(gmm.density(o,0)+gmm.density(o,1))
    //println(gmm_k.logDensity(o,1))
    //println(gmm.densityFunction.density())

}
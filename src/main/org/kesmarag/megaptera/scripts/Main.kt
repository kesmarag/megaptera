package org.kesmarag.megaptera.scripts

import org.kesmarag.megaptera.hmm.GaussianHiddenMarkovModel
import org.kesmarag.megaptera.hmm.GhmmClustering
import org.kesmarag.megaptera.utils.DataSet

fun main(args: Array<String>) {
    println(".:: megaptera v0.1 ::.")
    //val pat: String? = readLine()
    val dataSet = DataSet("/home/kesmarag/feat*",1)
    //val dataSet = DataSet(pat?:"*.csv",1)

    val hmm = GaussianHiddenMarkovModel(dataSet, 3, 1, 0)


    //hmm.update()
   // val clustering = GhmmClustering(dataSet,10,3,1)
    //clustering.training()

}
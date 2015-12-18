package org.kesmarag.megaptera.hmm

import org.kesmarag.megaptera.distributions.GaussianMixtureDensity
import org.kesmarag.megaptera.kmeans.Kmeans
import org.kesmarag.megaptera.utils.DataSet
import org.kesmarag.megaptera.utils.ObservationSet
import org.kesmarag.megaptera.utils.Owner
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class GaussianHiddenMarkovModel(var dataSet: DataSet,
                                val states: Int = 3,
                                val mixtures: Int = 1,
                                id: Int = 0) : Owner(id) {
    public val cores = Math.min(Runtime.getRuntime().availableProcessors(), 8)
    public var isUpdated: Boolean = false
        private set
    private var D: Int = dataSet.observationLength
    private var members: MutableList<Int> = arrayListOf()
    public var pi: DoubleArray = DoubleArray(states)
    public var aij: Array<DoubleArray> = Array(states, { DoubleArray(states) })
    public var emissions = emptyArray<GaussianMixtureDensity>()

    init {
        updateMemberList()
        paInit()
        emissionsInit()
        //println(totalPosterior())
        if (states < 2) {
            throw IllegalArgumentException("number of states must be greater than one")
        }

    }


    private fun emissionsInit(): Unit {
        val weights: DoubleArray = DoubleArray(mixtures)
        weights.fill(1.0 / (mixtures.toDouble()))

        val kmeans = Kmeans(dataSet, states * mixtures, 0)
        var means: Array<Array<DoubleArray>> =
                Array(states, { Array(mixtures, { DoubleArray(D) }) })
        var variances: Array<Array<DoubleArray>> =
                Array(states, { Array(mixtures, { DoubleArray(D) }) })
        for (k in 0..states - 1) {
            for (m in 0..mixtures - 1) {
                means[k][m] = kmeans.centroids[k + states * m]
                //println(means[k][m].forEach { print("$it ") })
                variances[k][m] = kmeans.variances[k + states * m]
                //println(variances[k][m].forEach { print("$it ") })

            }
        }
        //means.forEach { println(it.forEach { it.forEach { println(it) } }) }
        println(weights[0])
        //kmeans.centroids.forEach { it.forEach { println(it) } }
        emissions = Array(states,
                { k ->
                    GaussianMixtureDensity(weights.clone(),
                            means[k].clone(),
                            variances[k].clone())

                })

        //emissions.forEach { println(it.means[0][0]) }
        //println("end of emissionsInit here...")
    }

    private fun paInit(): Unit {
        val element = 1.0 / states.toDouble()
        aij.forEach { it.fill(element) }
        pi.fill(element)
        //println("end of paInit here...")
    }

    private fun updateMemberList(): Unit {
        members = dataSet.members
                .mapIndexed { i, observationSet ->
                    Pair<Int, ObservationSet>(i, observationSet)
                }
                .filter { isOwned(it.second) }
                .map { it -> it.first }
                .toArrayList()
        //println("end of updateMemberList here...")
    }

    public fun update(): Unit {
        updateMemberList()
        if (!isUpdated && members.size > 0) {
            println("hmm is updating using ${members.size} assigned members...")
            isUpdated = true
            var newPost: Double = 1.0
            var prevPost: Double = 0.0

            var iters: Int = 1
            while (Math.abs((newPost - prevPost) / prevPost) > 3e-14 && iters <= 500 ) {
                println(iters)
                var post: Array<ForwardBackward?> = Array(members.size, { null })
                var threadPool: ExecutorService = Executors.newFixedThreadPool(cores)
                for (q in 0..members.size - 1) {
                    threadPool.execute(Runnable { post[q] = ForwardBackward(this, dataSet[members[q]]) })
                }
                threadPool.shutdown();
                threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
                baum_welch(post)
                prevPost = newPost
                newPost = totalPosterior(post)
                println("posterior = $newPost")
                iters++
            }
            //println("posterior after = $newPost")
            isUpdated = false
        } else {
            println("Nothing to do...")
        }

    }

    private fun baum_welch(fb: Array<ForwardBackward?>) {
        for (j in 0..states - 1) {
            var sum: Double = 0.0
            for (d in 0..members.size - 1) {
                sum += fb[d]!!.gamma[0][j]
            }
            for (k in 0..states - 1) {
                var sum2: Double = 0.0
                var sum3: Double = 0.0
                for (d in 0..members.size - 1) {
                    for (n in 1..fb[d]!!.observationSet.size - 1) {
                        sum2 += fb[d]!!.xi[n - 1][j][k]
                        sum3 += fb[d]!!.gamma[n - 1][j]
                    }

                }
                aij[j][k] = (sum2 / sum3)
                //println("aij[$j][$k] = ${aij[j][k]}")
            }
            pi[j] = sum / members.size.toDouble()
            // println(pi[j])
        }

        // Expectation Step
        var c: Array<Array<Array<DoubleArray>>> =
                Array(states, { Array(mixtures, { Array(members.size, { doubleArrayOf() }) }) })

        for (k in 0..states - 1) {
            for (m in 0..mixtures - 1) {
                for (d in 0..members.size - 1) {
                    c[k][m][d] = DoubleArray(fb[d]!!.observationSet.size)
                    for (n in 0..fb[d]!!.observationSet.size - 1) {
                        c[k][m][d][n] = emissions[k].mixtureContribution(fb[d]!!.observationSet[n], m) *
                                fb[d]!!.gamma[n][k]
                    }
                }
            }
        }
        // Maximization Step


        // mean values update
        for (k in 0..states - 1) {
            for (m in 0..mixtures - 1) {
                for (i in 0..D - 1) {
                    var sum1: Double = 0.0
                    var sum2: Double = 0.0
                    for (d in 0..members.size - 1) {
                        for (n in 0..fb[d]!!.observationSet.size - 1) {
                            sum1 += c[k][m][d][n] * dataSet[members[d]][n][i]
                            sum2 += c[k][m][d][n]
                        }
                    }
                    //println(sum1)
                    emissions[k].means[m][i] = sum1 / sum2
                   // println(emissions[k].means[m][i])
                }
            }
        }

        // emissions update
        for (k in 0..states - 1) {
            var sum1: Double = 0.0
            for (m in 0..mixtures - 1) {
                var sum2: Double = 0.0
                for (d in 0..members.size - 1) {
                    for (n in 0..fb[d]!!.observationSet.size - 1) {
                        sum1 += c[k][m][d][n]
                        sum2 += c[k][m][d][n]

                    }
                }
                emissions[k].weights[m] = sum2

            }
            for (m in 0..mixtures - 1) {
                emissions[k].weights[m] /= sum1
                //println(emissions[k].mixingCoefficients[m])
            }
        }

        // covariances update
        for (k in 0..states - 1) {
            for (m in 0..mixtures - 1) {
                for (i in 0..D - 1) {
                    var sum1: Double = 0.0
                    var sum2: Double = 0.0
                    for (d in 0..members.size - 1) {
                        for (n in 0..fb[d]!!.observationSet.size - 1) {
                            val diff: Double = dataSet[members[d]][n][i] - emissions[k].means[m][i]
                            sum2 += c[k][m][d][n]
                            sum1 += c[k][m][d][n] * diff * diff
                        }
                    }
                    emissions[k].variances[m][i] = (sum1 / sum2)

                }
            }
        }
    }

    public fun posterior(observationSet: ObservationSet): Double {
        val fb: ForwardBackward = ForwardBackward(this, observationSet)
        return fb.getPosterior()

    }

    public fun totalPosterior(): Double {
        var sum: Double = 0.0
        dataSet.members.forEach { sum += posterior(it) }
        return sum
    }

    public fun totalPosterior(fb: Array<ForwardBackward?>): Double {
        var sum: Double = 0.0
        for (x in fb) {
            sum += x!!.getPosterior()
        }
        return sum
    }


    override fun toString(): String {
        var str: String = "GaussianHiddenMarkovModel(states = $states, mixtures = $mixtures)\n"
        return str
    }

}
package org.kesmarag.megaptera.hmm

import org.kesmarag.megaptera.utils.ObservationSet

class ForwardBackward(var hmm: GaussianHiddenMarkovModel,
                      var observationSet: ObservationSet) {
    public var gamma: Array<DoubleArray> = Array(observationSet.size, { DoubleArray(hmm.pi.size) })
        private set
    public var xi: Array<Array<DoubleArray>> =
            Array(observationSet.size - 1, { Array(hmm.pi.size, { DoubleArray(hmm.pi.size) }) })
        private set
    private var alpha: Array<DoubleArray> = Array(observationSet.size, { DoubleArray(hmm.pi.size) })
    private var beta: Array<DoubleArray> = Array(observationSet.size, { DoubleArray(hmm.pi.size) })
    public var c: DoubleArray = DoubleArray(observationSet.size)
        private set

    init {
        forward()
        backward()
        calculations()
    }

    private fun forward(): Unit {
        for (n in 0..observationSet.size - 1) {
            forwardStep(n)
        }
    }

    private fun backward(): Unit {
        for (n in observationSet.size - 1 downTo 0) {
            backwardStep(n)
        }
    }

    private fun forwardStep(n: Int): Unit {
        if (n == 0) {
            c[0] = 0.0
            for (k in 0..hmm.pi.size - 1) {
                alpha[0][k] = hmm.pi[k] * hmm.emissions[k].density(observationSet[0])
                c[0] += alpha[0][k]
            }
            for (k in 0..hmm.pi.size - 1) {
                alpha[0][k] /= c[0]
            }
        } else {
            var delta: DoubleArray = DoubleArray(hmm.pi.size)
            for (k in 0..hmm.pi.size - 1) {
                var tmpSum = 0.0
                for (j in 0..hmm.pi.size - 1) {
                    tmpSum += alpha[n - 1][j] * hmm.aij[j][k]
                }
                delta[k] = hmm.emissions[k].density(observationSet[n]) * tmpSum
                //println("delta = ${delta[k]}")
                //println("delta = ${hmm.emissions[k].density(observationSet[n])}")
            }
            c[n] = delta.sum()
            for (k in 0..hmm.pi.size - 1) {
                alpha[n][k] = delta[k] / c[n]
            }
        }
    }

    private fun backwardStep(n: Int): Unit {
        if (n == observationSet.size - 1) {
            for (k in 0..hmm.pi.size - 1) {
                beta[n][k] = 1.0
            }

        } else {
            for (k in 0..hmm.pi.size - 1) {
                var tmpSum = 0.0
                for (j in 0..hmm.pi.size - 1) {
                    tmpSum += beta[n + 1][j] * hmm.emissions[j].density(observationSet[n + 1]) * hmm.aij[k][j]
                }
                beta[n][k] = tmpSum / c[n + 1]
            }

        }
    }

    private fun calculations(): Unit {
        for (n in 0..observationSet.size - 1) {
            for (k in 0..hmm.pi.size - 1) {
                gamma[n][k] = alpha[n][k] * beta[n][k]
            }
        }
        for (n in 1..observationSet.size - 1) {
            for (j in 0..hmm.pi.size - 1) {
                for (k in 0..hmm.pi.size - 1) {
                    xi[n - 1][j][k] = alpha[n - 1][j] * hmm.emissions[k].density(observationSet[n]) *
                            hmm.aij[j][k] * beta[n][k] / c[n]
                }
            }
        }

    }

    public fun getPosterior(): Double {
        var s: Double = 0.0
        c.forEach { s += Math.log(it) }
        return s
    }
}

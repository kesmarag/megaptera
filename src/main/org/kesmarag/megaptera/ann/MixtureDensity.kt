package org.kesmarag.megaptera.ann

import org.kesmarag.megaptera.linear.ColumnVector
import org.kesmarag.megaptera.linear.UpperTriangularMatrix
import org.kesmarag.megaptera.utils.softmax


class MixtureDensity {
    public val mixtures: Int
    public val dimension: Int
    public var weights: ColumnVector
    public var means: Array<ColumnVector>
    public var alphas: Array<UpperTriangularMatrix>
    private val ln2pi = Math.log(Math.PI)

    constructor(_mixtures: Int, _dimension: Int) {
        mixtures = _mixtures
        dimension = _dimension
        weights = ColumnVector(_mixtures)
        weights.fill(1.0 / _mixtures.toDouble())
        means = Array(_mixtures) { ColumnVector(_dimension) }
        alphas = Array(_mixtures) { UpperTriangularMatrix(_dimension) }
        for (m in 0.._mixtures - 1) {
            for (i in 0.._dimension - 1) {
                alphas[m][i, i] = 1.0
            }
        }
    }

    public fun hyperParameters(output: ColumnVector): Unit {
        if ((dimension * dimension + 3 * dimension + 2) * mixtures / 2 != output.dimension) {
            throw IllegalStateException("wrong dimension")
        }
        weights = output[0..mixtures - 1]
        weights = softmax(weights)
        //println(weights)
        var k = mixtures
        for (i in 0..mixtures - 1) {
            for (j in 0..dimension - 1) {
                means[i][j] = output[i * dimension + j + dimension]
                k++
            }
        }

        for (m in 0..mixtures - 1) {
            for (i in 0..dimension - 1) {
                alphas[m][i, i] = Math.exp(output[k])
                k++
            }
        }
        for (m in 0..mixtures - 1) {
            for (i in 0..dimension - 1) {
                for (j in i + 1..dimension - 1) {
                    alphas[m][i, j] = output[k]
                    k++
                }
            }
        }
    }

    public fun detSigma(m: Int) : Double{
        var d = 1.0
        for (i in 0..dimension - 1) {
            d *= alphas[m][i, i]*alphas[m][i, i]
        }
        return 1.0/d
    }

    public fun detSigma(): ColumnVector {
        val d = ColumnVector(mixtures)
        for (m in 0..mixtures - 1) {
            d[m] = detSigma(m)
        }
        return d
    }

    operator fun get(x: ColumnVector, m: Int): Double{
        val Ah = alphas[m]*(x-means[m])
       // println(Ah.norm2())
        return -0.5*(dimension*ln2pi + Math.log(detSigma(m))+Ah.norm2()) + Math.log(weights[m])
    }

    operator fun get(x: ColumnVector): Double{
        val logDensityArray: DoubleArray = DoubleArray(mixtures)
        for (m in 0..mixtures - 1) {
            logDensityArray[m] = this[x, m]
        }
        val maxLogDensity: Double = logDensityArray.max() ?: 1.0
        var sum: Double = 0.0
        for (m in 0..mixtures - 1) {
            sum += Math.exp(logDensityArray[m] - maxLogDensity)
        }
        return maxLogDensity + Math.log(sum)
    }

    public fun gamma(x: ColumnVector, m: Int): Double{
        val logDensityArray: DoubleArray = DoubleArray(mixtures)
        for (m in 0..mixtures - 1) {
            logDensityArray[m] = this[x, m]
        }
        var g = 0.0
        for (i in 0..mixtures- 1) {
            g += Math.exp(logDensityArray[i] - logDensityArray[m])
        }
        return 1.0/g
    }

    public fun derivative(out: ColumnVector): ColumnVector{
        val y = ColumnVector((dimension * dimension + 3 * dimension + 2) * mixtures / 2)
        val etta = Array(mixtures) {ColumnVector(dimension)}
        for (m in 0..mixtures-1){
            //etta[m] = x[dimension*(m+1)..(dimension*(m+1)+dimension-1)] - means[m]
            etta[m] =  means[m] - out
        }
        val xi = Array(mixtures) {ColumnVector(dimension)}
        for (m in 0..mixtures-1){
            xi[m] = alphas[m]*etta[m]
        }
        val phi = Array(mixtures) {ColumnVector(dimension)}
        for (m in 0..mixtures-1){
            phi[m] = alphas[m].t()*xi[m]
        }
        for (i in 0..mixtures-1){
            y[i] = weights[i] - gamma(out,i)
        }
        var k = mixtures
        for (i in 0..mixtures - 1) {
            for (j in 0..dimension - 1) {
                y[k] = phi[i][j]*gamma(out,i)
                k++
            }
        }
        for (i in 0..mixtures - 1) {
            for (j in 0..dimension - 1) {
                y[k] = gamma(out,i)*(-1.0+xi[i][j]*alphas[i][j,j]*etta[i][j])
                k++
            }
        }
        for (m in 0..mixtures - 1) {
            for (i in 0..dimension - 1) {
                for (j in i + 1..dimension - 1) {
                    y[k] = gamma(out,m)*xi[m][i]*etta[m][j]
                    k++
                }
            }
        }


        return y
    }

    public fun toFile() {

    }


}
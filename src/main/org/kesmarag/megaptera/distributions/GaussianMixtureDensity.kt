package org.kesmarag.megaptera.distributions

import org.kesmarag.megaptera.utils.Observation

class GaussianMixtureDensity(public var weights: DoubleArray,
                             public var means: Array<DoubleArray>,
                             public var variances: Array<DoubleArray>) {

    public fun logDensity(x: DoubleArray, m: Int): Double {
        var p = 0.0
        var s = 1.0
        for (d in 0..x.size - 1) {
            val dx = x[d] - means[m][d]
            p += -0.5 * dx * dx / ( variances[m][d])
            s *= (variances[m][d])
        }
        if (s.isNaN()) {
            println("s = $s")
        }
        p += -0.5 * x.size.toDouble() * Math.log(2 * Math.PI) - 0.5 * Math.log(s) + Math.log(weights[m])
        return p
    }

    public fun logDensity(observation: Observation, m: Int) = logDensity(observation.data, m)

    public fun logDensity(x: DoubleArray): Double {
        val logDensityArray: DoubleArray = DoubleArray(weights.size)
        for (m in 0..weights.size - 1) {
            logDensityArray[m] = logDensity(x, m)
        }
        val maxLogDensity: Double = logDensityArray.max() ?: 1.0
        var sum: Double = 0.0
        for (m in 0..weights.size - 1) {
            sum += Math.exp(logDensityArray[m] - maxLogDensity)
        }
        return maxLogDensity + Math.log(sum)
    }

    public fun logDensity(observation: Observation) = logDensity(observation.data)

    public fun density(x: DoubleArray, m: Int) = Math.exp(logDensity(x, m))

    public fun density(observation: Observation, m: Int) = density(observation.data, m)

    public fun density(x: DoubleArray) = Math.exp(logDensity(x))

    public fun density(observation: Observation) = density(observation.data)

    public fun mixtureContribution(x: DoubleArray, m: Int): Double {
        val logDensityArray: DoubleArray = DoubleArray(weights.size)
        for (i in 0..weights.size - 1) {
            logDensityArray[i] = logDensity(x, i)
        }
        var sum: Double = 0.0
        for (i in 0..weights.size - 1) {
            sum += Math.exp(logDensityArray[i] - logDensityArray[m])
        }
        return 1.0 / sum
    }

    public fun mixtureContribution(observation: Observation, m: Int) =
            mixtureContribution(observation.data, m)


}
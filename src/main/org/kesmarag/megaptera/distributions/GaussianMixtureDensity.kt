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
            s *=variances[m][d]
            //println("var[$m][$d] = ${variances[m][d]}")
        }
        //s = Math.exp(s)
       // println("s = $s")
        p += -0.5 * x.size.toDouble() * Math.log(2 * Math.PI) - 0.5 * Math.log(s) + Math.log(weights[m])
        return p
    }

    public fun logDensity(observation: Observation, m: Int): Double {
        return logDensity(observation.data, m)
    }

    public fun logDensity(x: DoubleArray): Double {
        var p = 0.0
        for (m in 0..weights.size - 1) {
            p += density(x, m)
        }
        return Math.log(p)
    }


    public fun logDensity(observation: Observation): Double {
        return logDensity(observation.data)
    }

    public fun density(x: DoubleArray, m: Int): Double {
        return Math.exp(logDensity(x, m))
    }

    public fun density(observation: Observation, m: Int): Double {
        return density(observation.data, m)
    }

    public fun density(x: DoubleArray): Double {
        return Math.exp(logDensity(x))
    }

    public fun density(observation: Observation): Double {
        return density(observation.data)
    }

    public fun mixtureContribution(x: DoubleArray, m: Int): Double {
        return density(x,m)/density(x)
    }

    public fun mixtureContribution(observation: Observation, m: Int): Double {
        return mixtureContribution(observation.data, m)
    }


}
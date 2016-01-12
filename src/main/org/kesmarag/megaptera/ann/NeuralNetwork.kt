package org.kesmarag.megaptera.ann

class NeuralNetwork(val inputLayerSize:Int,
                    val hiddenLayerSize:Int,
                    val outputLayerSize:Int) {
    init {
        println(".:: artificial neural network ::.")
    }

    override fun toString(): String {
        return "NeuralNetwork[]"
    }

}
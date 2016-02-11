import org.kesmarag.megaptera.ann.MixtureDensity
import org.kesmarag.megaptera.ann.MixtureDensityNetwork
import org.kesmarag.megaptera.linear.ColumnVector
import org.kesmarag.megaptera.linear.DenseMatrix
import org.kesmarag.megaptera.linear.UpperTriangularMatrix
import org.kesmarag.megaptera.linear.columnVectorOf

fun main(args: Array<String>) {
    val a = ColumnVector(3)
    a[0] = 1.0
    a[1] = 2.0
    a[2] = -1.0
    val U = UpperTriangularMatrix(3)
    U[0,0] = 1.0 ; U[0,1] = -2.0; U[0,2] = 1.5
    U[1,1] = 1.5 ; U[1,2] = 3.0
    U[2,2] = 2.0

    println(U*U.t())
    val W1 = DenseMatrix(3, 3)
    W1.randomize(0.0, 1.0)
    println(W1)
    println(W1*a)
}
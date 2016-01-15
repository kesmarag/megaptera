import org.kesmarag.megaptera.linear.*
import org.kesmarag.megaptera.utils.*

fun main(args: Array<String>) {
    val dense = DenseMatrix(30,30)
    val d = dense*dense
    val U = UpperTriangularMatrix(2)
    U[0,0] = 1.0 ; U[0,1] = 2.0 ; U[1,1] = 3.0
    println(U.inv())
    val S = U.inv().t()
    val a = ColumnVector(100)
    a.randomize(0.0,1.0)
    println(a)
}
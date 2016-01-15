import org.kesmarag.megaptera.linear.*
import org.kesmarag.megaptera.utils.*

fun main(args: Array<String>) {
    val U = UpperTriangularMatrix(10)
    U[0,0] = 10.0 ; U[0,1] = -1.0 ; U[1,1] = -2.0
    println(U)
    val L = LowerTriangularMatrix(10)
    L[0,0] = 1.0 ; L[1,0] = -1.0 ; L[1,1] = 2.0
    println(U.t())
    val UU = U*U.t()
    println(UU)
}
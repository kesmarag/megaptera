import org.kesmarag.megaptera.linear.columnVectorOf

fun main(args: Array<String>) {
    val vec1 = columnVectorOf(1.0,2.0,3.0)
    val vec2 = columnVectorOf(4.0,5.0,6.0)
    val vec3 = vec1 + vec2
    println("run it...")
}
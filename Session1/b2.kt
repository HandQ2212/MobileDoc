import kotlin.random.Random

const val max_element = 1000005;
fun solve(n : Int){
    when(n){
        1 -> println(1)
        2, 3 -> println("No Solution")
        4 -> println("2 4 1 3")
        else -> {
            println("YES")
            val result = mutableListOf<Int>()
            for (i in 1..n step 2)   result.add(i)
            for (i in 2..n step 2)   result.add(i)
            for (number in result) {
                print("$number ")
            }
        }
    }
}

fun main() {
    val n = Random.nextInt(1, max_element)
    println(n)
    solve(n)
}
import scala.collection.mutable

object Main {
  def main(args: Array[String]): Unit = {
    val list = mutable.ArrayDeque("a", "b", "c")
    val range  = List.range(1,10)
    var count = 0
    list += "d"

    val newList = list.map((item) => {
      count += 1
      item + s":$count"
    })

    println(s"${
      newList.mkString(",")} ${newList.lift(1).getOrElse(0)} ${newList.lift(10).getOrElse(-1)}")
    }
  }
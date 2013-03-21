package reactormonk
import Numeric.Implicits._

class Counter[A, B](counter: Map[A, B])(implicit num:Numeric[B]) {
  def +(key: A): Counter[A, B] = this.change(key, num.fromInt(1))
  def -(key: A): Counter[A, B] = this.change(key, num.fromInt(-1))
  def change(key: A, by: B): Counter[A, B] = Counter((counter + (key -> {by.+(apply(key)):B})))
  def ++(other: Counter[A, B]): Counter[A, B] = other.iterator.foldLeft(this)({case (counter, (key, count)) => counter.change(key, count)})
  def get(key: A): Option[B] = counter.get(key)
  def apply(key: A): B = counter.getOrElse(key, num.fromInt(0))
  def toMap(): Map[A, B] = counter
  def iterator(): Iterator[Tuple2[A, B]] = counter.iterator
  def toList(): List[Tuple2[A, B]] = counter.toList
  def toSeq(): Seq[Tuple2[A, B]] = counter.toSeq
  def empty(): Counter[A, B] = Counter[A, B]()
  def size: Int = counter.size
  override def equals(other: Any): Boolean = other match {
    case (other: Counter[A, B]) => counter.toMap.equals(other.toMap)
    case _ => false
  }
  override def hashCode = counter.hashCode
  override def toString: String = counter.toString.replaceFirst("Map", "Counter") // cheap but effective
}

object Counter {
  def apply[A, B: Numeric](): Counter[A, B] = apply(Map[A, B]())
  def apply[A, B: Numeric](counter: Map[A, B]): Counter[A, B] = { new Counter[A, B](counter) }
  def apply[A](count: Iterable[A]): Counter[A, Int] = count.foldLeft(apply[A, Int]())({case (counter, element) => counter + element})
}

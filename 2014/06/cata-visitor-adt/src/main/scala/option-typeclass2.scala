package com.pellucid.option.typeclass2

/** very close in spirit to modules a la ML */
trait OptionSig {

  type OptionInt

  type SomeInt <: OptionInt

  type None <: OptionInt

}

trait OptionOps[Sig <: OptionSig] {

  def some(x: Int): Sig#OptionInt
    
  def none: Sig#OptionInt
    
  def fold[T](opt: Sig#OptionInt)(ifNone: => T, ifSome: Int => T): T

}

class Show[Sig <: OptionSig](implicit ops: OptionOps[Sig]) {

  import ops._

  def show(opt: Sig#OptionInt): String = fold(opt)("None", x => s"Some($x)")

}

object Show {

  def apply[Sig <: OptionSig](implicit ops: OptionOps[Sig]): Show[Sig] =
    new Show[Sig]

}



trait ScalaOption extends OptionSig {

  type OptionInt = scala.Option[Int]

  type SomeInt = scala.Some[Int]

  type None = scala.None.type

}

object ScalaOption {

  implicit object Ops extends OptionOps[ScalaOption] {

    def some(x: Int): ScalaOption#OptionInt = Some(x)

    val none: ScalaOption#OptionInt = scala.None

    def fold[T](opt: ScalaOption#OptionInt)(ifNone: => T, ifSome: Int => T): T = opt match {
      case scala.None    => ifNone
      case scala.Some(x) => ifSome(x)
    }

  }

  // no need for the following line anymore!
  // implicit object Show extends Show[ScalaOption]

}



class Program[Sig <: OptionSig](implicit Ops: OptionOps[Sig]) extends App {
  import Ops._
  val MyShow = Show[Sig]
  import MyShow._
  val opt = some(42)
  println(show(opt)) // will print Some(42)
}


object MainWithScalaOption extends Program[ScalaOption]

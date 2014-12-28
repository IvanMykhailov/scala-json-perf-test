package imykhailov.perftest.converters

import imykhailov.model._


trait Converters[JsTree <: AnyRef] {

  trait ReadWrite[T <: AnyRef] {
    def read(js: JsTree): T
    def write(o: T): JsTree
  }


  def entityWithMapRW: ReadWrite[EntityWithMap]

  def entityWithSeqRW: ReadWrite[EntityWithSeq]

  def plainEntityRW: ReadWrite[PlainEntity]

  def complexEntityRW: ReadWrite[ComplexEntity]


  def stringToTree(str: String): JsTree

  def treeToString(tree: JsTree): String
}

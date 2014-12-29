package imykhailov.perftest.converters

import imykhailov.model.{ComplexEntity, PlainEntity, EntityWithSeq, EntityWithMap}

object DummyConverters extends Converters[AnyRef] {

  class DummyJsonReadWrite[T <:AnyRef] extends ReadWrite[T] {
    def read(js: AnyRef): T = js.asInstanceOf[T]
    def write(o: T): AnyRef = o
  }

  override def entityWithMapRW: ReadWrite[EntityWithMap] = new DummyJsonReadWrite[EntityWithMap]

  override def entityWithSeqRW: ReadWrite[EntityWithSeq] = new DummyJsonReadWrite[EntityWithSeq]

  override def plainEntityRW: ReadWrite[PlainEntity] = new DummyJsonReadWrite[PlainEntity]

  override def complexEntityRW: ReadWrite[ComplexEntity] = new DummyJsonReadWrite[ComplexEntity]


  override def stringToTree(str: String): AnyRef = str

  override def treeToString(tree: AnyRef): String = "undefined"

  var storedObj: Option[ComplexEntity] = None

  override def complexEntityToStr(o: ComplexEntity): String = {
    storedObj = Some(o)
    "undefined"
  }

  override def strToComplexEntity(str: String): ComplexEntity = storedObj.get
}

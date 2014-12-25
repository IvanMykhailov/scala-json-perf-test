package itest

import play.api.libs.json._
import imykhailov.model._


class PlayJsonTest extends BaseTest[JsValue] {

  implicit val entityWithMapFormat = Json.format[EntityWithMap] 
  
  implicit val entityWithSeqFormat = Json.format[EntityWithSeq]
  
  implicit val plainEntityFormat = Json.format[PlainEntity]
  
  implicit val complexEntityFormat = Json.format[ComplexEntity]
  
  
  class PlayJsonReadWrite[T](implicit f: Format[T]) extends ReadWrite[T] {
    def read(js: JsValue): T = js.as[T]
    def write(o: T): JsValue = Json.toJson(o)
  }
  
  override def entityWithMapRW: ReadWrite[EntityWithMap] = new PlayJsonReadWrite[EntityWithMap]
  
  override def entityWithSeqRW: ReadWrite[EntityWithSeq] = new PlayJsonReadWrite[EntityWithSeq]
  
  override def plainEntityRW: ReadWrite[PlainEntity] = new PlayJsonReadWrite[PlainEntity]
  
  override def complexEntityRW: ReadWrite[ComplexEntity] = new PlayJsonReadWrite[ComplexEntity]
  
  
  override def stringToTree(str: String): JsValue = Json.parse(str)
  
  override def treeToString(tree: JsValue): String = tree.toString()
}
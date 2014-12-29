package imykhailov.perftest.converters

import imykhailov.model.{ComplexEntity, EntityWithMap, EntityWithSeq, PlainEntity}
import org.json4s.Extraction
import org.json4s.JsonAST.JValue
import org.json4s.jackson.JsonMethods

import scala.reflect.Manifest


object Json4sJacksonConverters extends Converters[JValue] {


  class Json4sReadWrite[T <: AnyRef](implicit mf: Manifest[T]) extends ReadWrite[T] {
    implicit val formats =  org.json4s.DefaultFormats

    def read(js: JValue): T = Extraction.extract[T](js)(formats, mf)
    def write(o: T): JValue = Extraction.decompose(o)
  }



  def entityWithMapRW: ReadWrite[EntityWithMap] = new Json4sReadWrite[EntityWithMap]

  def entityWithSeqRW: ReadWrite[EntityWithSeq] = new Json4sReadWrite[EntityWithSeq]

  def plainEntityRW: ReadWrite[PlainEntity] = new Json4sReadWrite[PlainEntity]

  def complexEntityRW: ReadWrite[ComplexEntity] = new Json4sReadWrite[ComplexEntity]


  def stringToTree(str: String): JValue = JsonMethods.parse(str)

  def treeToString(tree: JValue): String = {
    JsonMethods.compact(JsonMethods.render(tree))
  }

  override def complexEntityToStr(o: ComplexEntity): String = {
    implicit val formats = org.json4s.DefaultFormats
    import org.json4s.jackson.Serialization.write
    write(o)
  }

  override def strToComplexEntity(str: String): ComplexEntity = {
    implicit val formats = org.json4s.DefaultFormats
    JsonMethods.parse(str).extract[ComplexEntity]
  }
}

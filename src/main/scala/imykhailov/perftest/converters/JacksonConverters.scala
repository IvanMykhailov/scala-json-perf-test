package imykhailov.perftest.converters

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import imykhailov.model.{ComplexEntity, EntityWithMap, EntityWithSeq, PlainEntity}
import mesosphere.jackson.CaseClassModule

import scala.reflect.{ClassTag, Manifest}

//import imykhailov.utils.Json._
//import com.fasterxml.jackson.core.`type`.TypeReference

object JacksonConverters extends Converters[String] {

  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)
  mapper.registerModule(CaseClassModule)

  class JacksonReadWrite[T <: AnyRef](implicit tag: ClassTag[T]) extends ReadWrite[T] {
    val clazz: Class[T] = tag.runtimeClass.asInstanceOf[Class[T]]

    def read(js: String): T = mapper.readValue(js, clazz)
    def write(o: T): String = mapper.writeValueAsString(o)
  }

  override def entityWithMapRW: ReadWrite[EntityWithMap] = new JacksonReadWrite[EntityWithMap]

  override def entityWithSeqRW: ReadWrite[EntityWithSeq] = new JacksonReadWrite[EntityWithSeq]

  override def plainEntityRW: ReadWrite[PlainEntity] = new JacksonReadWrite[PlainEntity]

  override def complexEntityRW: ReadWrite[ComplexEntity] = new JacksonReadWrite[ComplexEntity]


  override def stringToTree(str: String): String = str

  override def treeToString(tree: String): String = tree
}

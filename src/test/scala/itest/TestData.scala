package itest

import imykhailov.perftest.converters.Converters
import org.scalatest.FlatSpec
import imykhailov.model.EntityWithMap
import imykhailov.model.EntityWithSeq
import imykhailov.model.PlainEntity
import imykhailov.model.ComplexEntity
import org.scalatest.Matchers
import imykhailov.model.PlainEntity
import imykhailov.model.ComplexEntity
import imykhailov.model.PlainEntity
import imykhailov.utils.Tabulator


class TestData[JsTree <: AnyRef](conv: Converters[JsTree]) extends Matchers {

  val m = (1 to 30).map(i => (s"key-$i" -> s"value-$i")).toMap
  
  val entityWithMap = EntityWithMap(m)
  
  val entityWithMapJs = conv.entityWithMapRW.write(entityWithMap)
  
  val entityWithSeq = EntityWithSeq(m.keys.toSeq)
  
  val entityWithSeqJs = conv.entityWithSeqRW.write(entityWithSeq)
  
  val plainEntity = PlainEntity(55, "foobar", Some("somestring"))
  
  val plainEntityJs = conv.plainEntityRW.write(plainEntity)
  
  val complexEntity = {
    val l3 = ComplexEntity(None, plainEntity, Some(entityWithMap))
    val l2 = ComplexEntity(Some(l3), plainEntity, None)
    ComplexEntity(Some(l2), plainEntity, None)
  }
  
  val complexEntityJs = conv.complexEntityRW.write(complexEntity)
  
  val complexEntityString = conv.treeToString(complexEntityJs)
  

  def testConversion() {
    conv.entityWithMapRW.read(entityWithMapJs) shouldBe entityWithMap
    conv.entityWithSeqRW.read(entityWithSeqJs) shouldBe entityWithSeq
    conv.plainEntityRW.read(plainEntityJs) shouldBe plainEntity
    conv.complexEntityRW.read(complexEntityJs) shouldBe complexEntity

    if (complexEntityString != "undefined") {
      conv.complexEntityRW.read(conv.stringToTree(complexEntityString)) shouldBe complexEntity
    }

    val str = conv.complexEntityToStr(complexEntity)
    if (str != "undefined") {
      conv.strToComplexEntity(str) shouldBe complexEntity
    }

  }

}




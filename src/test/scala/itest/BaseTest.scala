package itest

import org.scalatest.FlatSpec
import imykhailov.model.EntityWithMap
import imykhailov.model.EntityWithSeq
import imykhailov.model.PlainEntity
import imykhailov.model.ComplexEntity
import org.scalatest.Matchers
import imykhailov.model.PlainEntity
import imykhailov.model.ComplexEntity
import imykhailov.model.PlainEntity


trait BaseTest[JsTree] extends FlatSpec with Matchers {
  
  trait ReadWrite[T] {
    def read(js: JsTree): T
    def write(o: T): JsTree
  }
  
  
  def entityWithMapRW: ReadWrite[EntityWithMap]
  
  def entityWithSeqRW: ReadWrite[EntityWithSeq]
  
  def plainEntityRW: ReadWrite[PlainEntity]
  
  def complexEntityRW: ReadWrite[ComplexEntity]
  
  
  def stringToTree(str: String): JsTree
  
  def treeToString(tree: JsTree): String
  
  
  lazy val m = (1 to 30).map(i => (s"key-$i" -> s"value-$i")).toMap
  
  lazy val entityWithMap = EntityWithMap(m)
  
  lazy val entityWithMapJs = entityWithMapRW.write(entityWithMap)
  
  lazy val entityWithSeq = EntityWithSeq(m.keys.toSeq)
  
  lazy val entityWithSeqJs = entityWithSeqRW.write(entityWithSeq)
  
  lazy val plainEntity = PlainEntity(55, "foobar", Some("somestring"))
  
  lazy val plainEntityJs = plainEntityRW.write(plainEntity)
  
  lazy val complexEntity = {
    val l3 = ComplexEntity(None, plainEntity, Some(entityWithMap))
    val l2 = ComplexEntity(Some(l3), plainEntity, None)
    ComplexEntity(Some(l2), plainEntity, None)
  }
  
  lazy val complexEntityJs = complexEntityRW.write(complexEntity)
  
  lazy val complexEntityString = treeToString(complexEntityJs)
  
  val objectSerialyzeTests = Map(
    "Maps" -> {() => { entityWithMapRW.write(entityWithMap) }},    
    "Sequences" -> {() => { entityWithSeqRW.write(entityWithSeq) }},
    "Plain objects" -> {() => { plainEntityRW.write(plainEntity) }},
    "Complex objects" -> {() => { complexEntityRW.write(complexEntity) }}
  )
  
  val objectDeserialyzeTests = Map(
    "Maps" -> {() => { entityWithMapRW.read(entityWithMapJs) }},    
    "Sequences" -> {() => { entityWithSeqRW.read(entityWithSeqJs) }},
    "Plain objects" -> {() => { plainEntityRW.read(plainEntityJs) }},
    "Complex objects" -> {() => { complexEntityRW.read(complexEntityJs) }}
  )
  
  val stringifyTests = Map(
    "toString" -> {() => treeToString(complexEntityJs)},
    "parse" -> {() => {stringToTree(complexEntityString)}}
  ) 
  
  
  val tests = Map(
    "Obj-to-Tree" -> objectSerialyzeTests,
    "Tree-to-Obj" -> objectDeserialyzeTests,
    "Tree" -> stringifyTests
  )
  
  val tests2 = (for {
    (category, categoryTests) <- tests.toSeq
    (testName, test) <- categoryTests
  } yield {
    (category + ":" + testName, test)
  }).sortBy(_._1)
    
  def performance(name: String, series: Int, times: Long)(block: => Unit) = {
    val perfs = (for (s <- 1 to series) yield {
        var i = 0
        val t0 = System.nanoTime()
        while (i < times) {
            block
            i += 1
        }
        val t1 = System.nanoTime()
        val perf = times * 1000000000l / (t1 - t0)
        println(s"$name elapsed: ${(t1 - t0) / 1000000} ms, performance: $perf op/s")
        perf
    }).tail
    val avg = perfs.reduce(_ + _) / perfs.size
    println(s"$name avg performance: $avg op/s")
    avg
  }
  
  "Conversion" should "be bidirectional and correct" in {
    entityWithMapRW.read(entityWithMapJs) shouldBe entityWithMap
    entityWithSeqRW.read(entityWithSeqJs) shouldBe entityWithSeq
    plainEntityRW.read(plainEntityJs) shouldBe plainEntity
    complexEntityRW.read(complexEntityJs) shouldBe complexEntity
    
    complexEntityRW.read(stringToTree(complexEntityString)) shouldBe complexEntity
  }
  
  
  "Performance" should "be tested" in {
    val rez = tests2.map { case (name, code) => name -> performance(name, 5, 100000){code()} }
    printResult("Play Json", rez)
  }
  
  
  def printResult(jsonLibName: String, rez: Seq[(String, Long)]) = {
    val data = Seq(Seq("Test", "op/s")) ++ rez.map(t => Seq(t._1, t._2.toString))
    println(Tabulator.format(data))
  }
}



object Tabulator {
  def format(table: Seq[Seq[Any]]) = table match {
    case Seq() => ""
    case _ => 
      val sizes = for (row <- table) yield (for (cell <- row) yield if (cell == null) 0 else cell.toString.length)
      val colSizes = for (col <- sizes.transpose) yield col.max
      val rows = for (row <- table) yield formatRow(row, colSizes)
      formatRows(rowSeparator(colSizes), rows)
  }

  def formatRows(rowSeparator: String, rows: Seq[String]): String = (
    rowSeparator :: 
    rows.head :: 
    rowSeparator :: 
    rows.tail.toList ::: 
    rowSeparator :: 
    List()).mkString("\n")

  def formatRow(row: Seq[Any], colSizes: Seq[Int]) = {
    val cells = (for ((item, size) <- row.zip(colSizes)) yield if (size == 0) "" else ("%" + size + "s").format(item))
    cells.mkString("|", "|", "|")
  }

  def rowSeparator(colSizes: Seq[Int]) = colSizes map { "-" * _ } mkString("+", "+", "+")
}
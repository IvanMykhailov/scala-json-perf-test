package itest

import imykhailov.perftest.converters._
import imykhailov.utils.Tabulator
import itest.model.{TaskRez, TestTask}
import org.json4s.JsonAST.JValue
import org.scalatest.{Matchers, FlatSpec}
import play.api.libs.json.JsValue


class PerformanceTest extends FlatSpec with Matchers {

  val testLibraries = Seq(
    new TestedLib[JsValue] {def name = "PlayJson";       def converters = PlayJsonConverters},
    new TestedLib[JValue]  {def name = "json4s/native";  def converters = Json4sNativeConverters},
    new TestedLib[JValue]  {def name = "json4s/jackson"; def converters = Json4sJacksonConverters},
    new TestedLib[String]  {def name = "jackson";        def converters = JacksonConverters},
    new TestedLib[AnyRef]  {def name = "Dummy";          def converters = DummyConverters}
  )



  "Conversion" should "be correct" in {
    testLibraries.map(_.data.testConversion())
  }


  "Performance" should "be tested" in {
    val rawResults = for {
      lib <-testLibraries
      task <- lib.testTasks
    } yield {
      val fullName = task.group  + ":" + task.name + ":" + lib.name
      val rezOps: Long = performance(fullName, 5, 50000) {
        task.task()
      }
      TaskRez(task.group, task.name, lib.name, rezOps)
    }

    val header = Seq("Group", "Test") ++ testLibraries.map(_.name).sorted

    val dataToPrint = rawResults
      .groupBy(r => r.group + r.name)
      .toSeq
      .sortBy(_._1)
      .map(_._2)
      .map { row =>
        val group = row(0).group
        val name  = row(0).name
        Seq(group, name) ++ row.sortBy(_.libName).map(rez => formatOps(rez.ops))
      }

    val toPrint = Seq(header) ++ dataToPrint
    println(Tabulator.format(toPrint))
  }


  def formatOps(ops: Long): String = {
    "%.1f".format(ops.toDouble / 1000)
  }

  private def performance(name: String, series: Int, times: Long)(block: => Unit) = {
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
}

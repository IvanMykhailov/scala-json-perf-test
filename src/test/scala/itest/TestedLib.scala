package itest

import imykhailov.perftest.converters.Converters
import itest.model.TestTask

trait TestedLib[JsTree <: AnyRef] {

  def converters: Converters[JsTree]

  def name: String

  val data = new TestData[JsTree](converters)

  val testTasks: Seq[TestTask] = {
    Seq(
      TestTask("1. obj-to-AST",  "maps",            () => { converters.entityWithMapRW.write(data.entityWithMap) }),
      TestTask("1. obj-to-AST",  "sequences",       () => { converters.entityWithSeqRW.write(data.entityWithSeq) }),
      TestTask("1. obj-to-AST",  "plain objects",   () => { converters.plainEntityRW.write(data.plainEntity) }),
      TestTask("1. obj-to-AST",  "complex objects", () => { converters.complexEntityRW.write(data.complexEntity) }),

      TestTask("2. AST-to-obj",  "maps",            () => { converters.entityWithMapRW.read(data.entityWithMapJs) }),
      TestTask("2. AST-to-obj",  "sequences",       () => { converters.entityWithSeqRW.read(data.entityWithSeqJs) }),
      TestTask("2. AST-to-obj",  "plain objects",   () => { converters.plainEntityRW.read(data.plainEntityJs) }),
      TestTask("2. AST-to-obj",  "complex objects", () => { converters.complexEntityRW.read(data.complexEntityJs) }),

      TestTask("3. AST",         "to string",       () => { converters.treeToString(data.complexEntityJs) }),
      TestTask("3. AST",         "parse string",    () => { converters.stringToTree(data.complexEntityString)} ),

      TestTask("4. full cycle",  "direct str",      () => {
        val str = converters.complexEntityToStr(data.complexEntity)
        converters.strToComplexEntity(str)
      } ),

      TestTask("4. full cycle",   "through AST",    () => {
        val tree = converters.complexEntityRW.write(data.complexEntity)
        val str = converters.treeToString(tree)
        val parsedTree = converters.stringToTree(str)
        converters.complexEntityRW.read(parsedTree)
      } )
    )
  }
}
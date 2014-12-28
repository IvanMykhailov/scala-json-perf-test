package itest

import imykhailov.perftest.converters.Converters
import itest.model.TestTask

trait TestedLib[JsTree <: AnyRef] {

  def converters: Converters[JsTree]

  def name: String

  val data = new TestData[JsTree](converters)

  val testTasks: Seq[TestTask] = {
    Seq(
      TestTask("obj-to-tree", "maps",            () => { converters.entityWithMapRW.write(data.entityWithMap) }),
      TestTask("obj-to-tree", "sequences",       () => { converters.entityWithSeqRW.write(data.entityWithSeq) }),
      TestTask("obj-to-tree", "plain objects",   () => { converters.plainEntityRW.write(data.plainEntity) }),
      TestTask("obj-to-tree", "complex objects", () => { converters.complexEntityRW.write(data.complexEntity) }),

      TestTask("tree-to-obj", "maps",            () => { converters.entityWithMapRW.read(data.entityWithMapJs) }),
      TestTask("tree-to-obj", "sequences",       () => { converters.entityWithSeqRW.read(data.entityWithSeqJs) }),
      TestTask("tree-to-obj", "plain objects",   () => { converters.plainEntityRW.read(data.plainEntityJs) }),
      TestTask("tree-to-obj", "complex objects", () => { converters.complexEntityRW.read(data.complexEntityJs) }),

      TestTask("tree",        "to string",       () => { converters.treeToString(data.complexEntityJs) }),
      TestTask("tree",        "parse string",    () => { converters.stringToTree(data.complexEntityString)} )
    )
  }
}

package itest.model

case class TestTask(group: String, name: String, task: ()=> Unit)



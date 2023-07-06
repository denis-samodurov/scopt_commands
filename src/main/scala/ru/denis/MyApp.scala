package ru.denis

sealed trait Command
case class Command1(foo: Int, bar: String) extends Command
case class Command2(baz: Boolean, qux: Double) extends Command

object MyApp extends App {
  def parse(args: Array[String]) = {
    val defaultCommand =  args(0) match {
      case "command1" => Command1(0, "")
      case "command2" => Command2(false, 0)
    }

    val parser = new scopt.OptionParser[Command]("myapp") {
      head("myapp", "1.0")

      // Определение параметров для Command1
      cmd("command1")
        .children(
          opt[Int]('f', "foo")
            .required()
            .action((x, c) => c.asInstanceOf[Command1].copy(foo = x))
            .text("Параметр foo"),
          opt[String]('b', "bar")
            .required()
            .action((x, c) => c.asInstanceOf[Command1].copy(bar = x))
            .text("Параметр bar")
        )

      // Определение параметров для Command2
      cmd("command2")
        .children(
          opt[Boolean]('z', "baz")
            .required()
            .action((x, c) => c.asInstanceOf[Command2].copy(baz = x))
            .text("Параметр baz"),
          opt[Double]('q', "qux")
            .required()
            .action((x, c) => c.asInstanceOf[Command2].copy(qux = x))
            .text("Параметр qux")
        )
    }

    parser.parse(args, defaultCommand) match {
      case Some(command: Command1) =>
        // Логика для выполнения Command1
        println(s"Command1: foo=${command.foo}, bar=${command.bar}")

      case Some(command: Command2) =>
        // Логика для выполнения Command2
        println(s"Command2: baz=${command.baz}, qux=${command.qux}")

      case _ =>
        parser.help("help")
    }
  }
}
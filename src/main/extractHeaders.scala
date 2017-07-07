interp.load.ivy("com.lihaoyi" % s"ammonite-shell_2.12.2" % "1.0.0")
@
import ammonite.ops._, ImplicitWd._
import ammonite.shell._

val tutFolder = "tut"
val section = """^# (.*)""".r
val subSectionId = """^## (.*)\{#(.*)\}""".r
val subSectionNoId = """^## (.*)""".r
val sectionName = """\d-(.*)\.md""".r

def mkSub(p: Path) = {
    val lines = try {
      (%%grep("^#", p.toString)).out.lines
    } catch {
        case _ => List()
    }
    val vs = lines map { 
        case section(x)        => s"* [$x](${p.name})"
        case subSectionId(a,b) => s"  * [${a.trim}](${p.name}#$b)"
        case subSectionNoId(x) => s"  * $x" 
        case _ => ""
    }
    vs filter (_.nonEmpty) mkString "\n"
}

val files = ls! pwd / 'tut |? (_.name matches """\d+-.*\.md""")
val result = files map(mkSub) mkString "\n"

println("# Summary\n")
println(result)
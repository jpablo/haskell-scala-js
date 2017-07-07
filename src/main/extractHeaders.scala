val tutFolder = "../"
val hId = """##(.*)\{#(.*)\}""".r
val hNoId = """##(.*)""".r

val sectionName = """\d-(.*)\.md""".r
def mkSectionName(s: String) = {
    s match {
        case sectionName(n) => n.replace("-", " ").capitalize
        case x => x
    }
}

def mkSub(p: ammonite.ops.Path) = {
    val lines = try {
      (%%grep("^## ", p.toString)).out.lines
    } catch {
        case e => List()
    }
    val vs = lines map { 
        case hId(a,b) => s"  * [${a.trim}](${p.name}#$b)"
        case hNoId(x) => s"  * $x" 
    }
    vs.mkString("\n")    
}



val files = ls! pwd/up/'tut

println(files.map(x => s"* [${mkSectionName(x.name)}](${x.name})\n" + mkSub(x)).mkString("\n"))
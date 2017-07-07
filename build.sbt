enablePlugins(TutPlugin)
enablePlugins(GitBookPlugin)
enablePlugins(GhpagesPlugin)
enablePlugins(PreprocessPlugin)

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.1",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "Python To Scala"
)


scalacOptions in Tut := Seq("-Dscala.color")
git.remoteRepo := "git@github.com:jpablo/python-scala.git"
ghpagesNoJekyll := true
sourceDirectory in GitBook := tutTargetDirectory.value
makeSite := makeSite.dependsOn(tutQuick).value
previewLaunchBrowser := false

sourceDirectory in Preprocess := tutTargetDirectory.value

preprocessVars in Preprocess := Map("VERSION" -> version.value, "DATE" -> (new java.util.Date).toString)


name := "graph_sv_genotyper"

version := "1.0"

scalaVersion := "2.12.1"

libraryDependencies += "junit" % "junit" % "4.10" % "test"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"

libraryDependencies += "com.trueaccord.scalapb" %% "scalapb-runtime" % "0.5.47"

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value
)
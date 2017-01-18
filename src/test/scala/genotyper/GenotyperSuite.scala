package genotyper

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import vg.vg._

@RunWith(classOf[JUnitRunner])
class GenotyperSuite extends FunSuite{

  val nodeA = new Node("AT", "A", 1)
  val nodeB = new Node("TA", "B", 2)
  val nodeC = new Node("GG", "C", 3)
  val edge1 = new Edge(nodeA.id, nodeB.id, true, true, 0)
  val edge2 = new Edge(nodeB.id, nodeC.id, true, true, 0)
  val edge3 = new Edge(nodeA.id, nodeC.id, true, true, 0)
  val map1 = new Mapping(Some(Position(nodeA.id)))
  val map2 = new Mapping(Some(Position(nodeB.id)))
  val map3 = new Mapping(Some(Position(nodeC.id)))
  val ref = new Path("ref", Seq(map1, map2, map3))
  val del1 = new Path("del1", Seq(map1, map3))
  val read1 = new Alignment(path=Some(Path(mapping=Seq(map1, map2))), name="read1")
  val read2 = new Alignment(path=Some(Path(mapping=Seq(map1, map3))), name="read2")

  val genotyper = new Genotyper(ref, Seq(del1))

  test("Very simple case") {
    val edgeCounts = genotyper.getEdgeCounts(Seq(read1, read2))
    assert(edgeCounts == Map(((Some(nodeA.id), Some(nodeB.id)), 1), ((Some(nodeA.id), Some(nodeC.id)), 1)))
    val genotypes = genotyper.getGenotypes(edgeCounts)
    assert(genotypes(Some(nodeA.id)).counts == Map(((Some(nodeA.id), Some(nodeB.id)), 1),
      ((Some(nodeA.id), Some(nodeC.id)), 1)))
  }
}

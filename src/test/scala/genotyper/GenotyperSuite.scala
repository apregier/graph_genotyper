package genotyper

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GenotyperSuite extends FunSuite{

  val nodeA = new Node("a")
  val nodeB = new Node("b")
  val nodeC = new Node("c")
  val edge1 = new Edge(nodeA, nodeB)
  val edge2 = new Edge(nodeB, nodeC)
  val edge3 = new Edge(nodeA, nodeC)
  val ref = new Path(Seq(edge1, edge2))
  val del1 = new Path(Seq(edge3))
  val read1 = new Alignment(new Path(Seq(edge1)))
  val read2 = new Alignment(new Path(Seq(edge3)))

  val genotyper = new Genotyper(ref, Seq(del1))

  test("Very simple case") {
    val edgeCounts = genotyper.getEdgeCounts(Seq(read1, read2))
    assert(edgeCounts == Map((edge1, 1), (edge3, 1)))
    val genotypes = genotyper.getGenotypes(edgeCounts)
    assert(genotypes(nodeA).counts == Map((edge1, 1), (edge3, 1)))
  }
}

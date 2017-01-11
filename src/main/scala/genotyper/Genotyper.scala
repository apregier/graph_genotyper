import scala.collection._

package object genotyper {

  class Node(val name: String) {}

  class Alignment(val path: Path) {}

  class Path(val edges: Seq[Edge]) {}

  class Edge(val nodeA: Node, val nodeB: Node) {}

  class Genotype(val counts: GenMap[Edge, Int]) {}

  class Genotyper(val ref: Path, val svs: GenSeq[Path]) {
    def getEdgeCounts(alignments: GenSeq[Alignment]): GenMap[Edge, Int] = {
      alignments.flatMap(_.path.edges.map((_, 1))).groupBy(_._1).map{case(edge, list) => (edge, list.size)}
    }

    def getGenotypes(edgeCounts: GenMap[Edge, Int]): GenMap[Node, Genotype] = {
      edgeCounts.groupBy(_._1.nodeA).map{case(n, e) => (n, new Genotype(e))}
    }
  }
}

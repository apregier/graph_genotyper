import scala.collection._
import vg.vg._

package object genotyper {
  class Genotype(val counts: GenMap[(Option[Long], Option[Long]), Int]) {}

  class Genotyper(val ref: Path, val svs: GenSeq[Path]) {
    def getEdgeCounts(alignments: GenSeq[Alignment]): GenMap[(Option[Long], Option[Long]), Int] = {
      alignments.flatMap(a => getNodePairs(a.path.map(_.mapping)))
        .groupBy(_._1).map{case(edge, list) => (edge, list.size)}
    }

    def getNodePairs(path: Option[Seq[Mapping]]): GenSeq[((Option[Long], Option[Long]), Int)] = path match{
      case Some(s) => (for (i <- 0 to s.length - 2) yield ((s(i).position.map(_.nodeId),
        s(i+1).position.map(_.nodeId)), 1))
    }

    def getGenotypes(edgeCounts: GenMap[(Option[Long], Option[Long]), Int]): GenMap[Option[Long], Genotype] = {
      edgeCounts.groupBy(_._1._1).map{case(n, e) => (n, new Genotype(e))}
    }
  }
}

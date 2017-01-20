package stream

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import vg.vg.Alignment
import com.trueaccord.scalapb.GeneratedMessageCompanion

/**
  * Created by aregier on 1/18/17.
  */
@RunWith(classOf[JUnitRunner])
class StreamSuite extends FunSuite{
  test("Read a gam file") {
    def printAlignment(alignmentMessage: Alignment) = {
      println(alignmentMessage.name)
    }
    val s = new ProtobufStream[Alignment]
    s.allRecordsFromFile("/Users/aregier/coursera/parallel/graph_sv_genotyper/src/test/resources/test.gam")(printAlignment)
  }
}

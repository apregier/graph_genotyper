package stream

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import vg.vg.Alignment
import scala.io.Source
import com.trueaccord.scalapb.GeneratedMessageCompanion

/**
  * Created by aregier on 1/18/17.
  */
@RunWith(classOf[JUnitRunner])
class StreamSuite extends FunSuite{
  test("Read a gam file") {
    val testGamFile=getClass.getResource("/test.gam")
    def printAlignment(alignmentMessage: Alignment) = {
      println(alignmentMessage.toString)
    }
    val s = new ProtobufStream[Alignment]
    s.allRecordsFromFile(testGamFile.getPath)(printAlignment)
  }
}

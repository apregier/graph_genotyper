package stream

import java.io.{BufferedInputStream, DataInputStream, File, FileInputStream}
import java.util.zip.GZIPInputStream

import com.trueaccord.scalapb.GeneratedMessageCompanion
import com.trueaccord.scalapb.GeneratedMessage
import com.trueaccord.scalapb.Message
import com.google.protobuf.CodedInputStream

/**
  * Original copied from https://gist.github.com/kevinweil/243400 on 1/18/2017
  */
class ProtobufStream[M <: GeneratedMessage with Message[M]](implicit cmp: GeneratedMessageCompanion[M]) {
  var file: File = null
  var bufferedIn: BufferedInputStream = null

  // ensure we're not at EOF
  def streamHasNext: Boolean = {
    bufferedIn.mark(1)
    val nextByte = bufferedIn.read()
    bufferedIn.reset()
    nextByte != -1
  }

  def allRecordsFromFile(fileName: String)(f: M => Unit) {
    file = new File(fileName)
    bufferedIn = new BufferedInputStream(new FileInputStream(file), 2048)
    lazy val stream = CodedInputStream.newInstance(new DataInputStream(new GZIPInputStream(bufferedIn, 2048)))

    while (streamHasNext) {
      val count = stream.readRawVarint64().toInt
      println("count: "+count)
      for (i <- 0 to count-1) {
        val arraySize = stream.readRawVarint32()
        println("Read " + arraySize + " bytes")
        val bytes = stream.readRawBytes(arraySize)
        f(cmp.parseFrom(bytes))
      }
    }
  }
}

import com.google.protobuf.Message
import java.io.{BufferedReader, BufferedInputStream, DataInputStream, File, FileInputStream, InputStream, InputStreamReader}
import java.util.zip.GZIPInputStream
import scala.reflect.Manifest

/**
  * Copied from https://gist.github.com/kevinweil/243400
  */
class ProtobufStream[M <: Message](implicit man: Manifest[M]) {
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

    lazy val stream = if (fileName.endsWith(".gz")) {
      new DataInputStream(new GZIPInputStream(bufferedIn, 2048))
    } else {
      new DataInputStream(bufferedIn)
    }

    val record = man.erasure.newInstance.asInstanceOf[M]

    while (streamHasNext) {
      val arraySize = stream.readInt()
      val bytes: Array[Byte] = new Array[Byte](arraySize)
      stream.readFully(bytes)

      record.parseFrom(bytes)
      f(record)
    }

    stream.close()
  }
}

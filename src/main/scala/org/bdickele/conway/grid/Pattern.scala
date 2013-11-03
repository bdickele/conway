package org.bdickele.conway.grid

import java.net.URL
import java.io.File
import scala.io.Source

/**
 * User: bdickele
 *
 * Get patterns at http://www.argentum.freeserve.co.uk/lex.htm
 *
 * Date: 11/2/13
 */
class Pattern(val name: String, val pattern: Array[Array[Int]]) {

  override def toString = name
}

object Pattern {

  val File_Folder = "/patterns/"
  val Random = Pattern("Random")
  val Empty = Pattern("Empty")

  def apply(name: String) = new Pattern(name, Array.empty[Array[Int]])

  def patternList: List[Pattern] = {

    val folderURL: URL = Pattern.getClass.getResource(File_Folder)
    val folder: File = new File(folderURL.getPath)
    val files = folder.listFiles().toList

    def patternListAcc(fileNames: List[File], acc: List[Pattern]): List[Pattern] = fileNames match {
      case Nil => acc
      case fileName :: xs => {
        val fileLines = Source.fromFile(fileName).getLines().toList
        val patternName = fileLines.head
        val gridLines = fileLines.tail
        val nbLines = gridLines.size
        val nbColumns = gridLines.head.size

        val patternGrid = Array.fill[Int](nbLines, nbColumns)(0)

        var j = 0
        for (i <- 0 until nbLines) {
          j = 0
          for (c <- gridLines(i).trim.toCharArray) {
            if (c == '0' || c == 'O' || c == 'o' || c == '*') patternGrid(i)(j) = 1
            j += 1
          }
        }
        //patternGrid.foreach(r => println(r.mkString(" ")))
        patternListAcc(xs, new Pattern(patternName, patternGrid) :: acc)
      }

    }

    Random :: patternListAcc(files, Nil).sortBy(_.name)
    //List(Random)
  }
}

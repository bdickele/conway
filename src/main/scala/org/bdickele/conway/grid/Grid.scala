package org.bdickele.conway.grid

import org.bdickele.conway.grid.Grid.size

/**
 * User: bdickele
 * Date: 11/2/13
 */
class Grid(val grid: Array[Array[Int]]) {

  //grid.foreach(r => println(r.mkString(" ")))

  def get(x: Int, y: Int): Int = grid(x)(y)

  def isEmpty(): Boolean = grid.indexWhere(row => row.indexOf(1) > -1) < 0

  def isDifferentFrom(other: Grid): Boolean = {

    def areCellsDifferent(x: Int, y: Int): Boolean = {
      if (x >= size) false
      else if (y >= size) areCellsDifferent(x + 1, 0)
      else if (get(x, y) == other.get(x, y)) areCellsDifferent(x, y + 1)
      else true
    }

    areCellsDifferent(0, 0)
  }

  def switchCell(x: Int, y: Int) {
    if (x >= 0 && x < Grid.size && y >= 0 && y < Grid.size)
      grid(x)(y) = Math.abs(grid(x)(y) - 1)
  }

}

object Grid {

  var size = 100

  def apply(grid: Array[Array[Int]] = Array.fill[Int](size, size)(0)): Grid = new Grid(grid)
}

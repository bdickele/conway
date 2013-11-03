package org.bdickele.conway.grid

import scala.util.Random

/**
 * User: bdickele
 * Date: 11/2/13
 */
object GridComputation {

  private def createNewArray: Array[Array[Int]] = Array.fill[Int](Grid.size, Grid.size)(0)

  def createRandomGrid(): Grid = {
    val r = new Random()

    val grid = createNewArray

    for {x <- 0 until Grid.size
         y <- 0 until Grid.size} {
      grid(x)(y) = if (r.nextInt(9) < 2) 1 else 0
    }

    Grid(grid)
  }

  def createPatternGrid(pattern: Pattern): Grid = pattern match {
    case Pattern.Empty => Grid()
    case Pattern.Random => createRandomGrid()
    case _ => {
      val grid = createNewArray
      val patternGrid = pattern.pattern

      val gridWidth = grid.length
      val gridHeight = grid.length
      val patternWidth = patternGrid.length
      val patternHeight = patternGrid.head.length

      val startX = (gridWidth - patternWidth) / 2
      val startY = (gridHeight - patternHeight) / 2


      for {i <- 0 until patternWidth
           j <- 0 until patternGrid(i).length}
        grid(startX + i)(startY + j) = patternGrid(i)(j)

      Grid(grid)
    }
  }

  def computeNewGrid(old: Grid): Grid = {
    val oldGrid = old.grid
    val newGrid = createNewArray

    for {x <- 0 until Grid.size
         y <- 0 until Grid.size} {
      var sum = 0
      // Sum of living cells around current cell
      for {i <- -1 until 2
           j <- -1 until 2} {
        val oX = x + i
        val oY = y + j

        if ((oX >= 0) && (oY >= 0) && (oX < Grid.size) && (oY < Grid.size) && !(oX == x && oY == y)
          && oldGrid(oX)(oY) == 1) {
          sum += 1
        }
      }

      // ==== Core rules ====

      if (oldGrid(x)(y) == 1) {
        // Any live cell with fewer than 2 live neighbours dies, as if caused by under-population
        // Any live cell with 2 or 3 live neigbours lives on
        // Any live cell with more than 3 live neighbours dies, as if by overcrowding.
        if (sum < 2 || sum > 3) newGrid(x)(y) = 0
        else newGrid(x)(y) = 1
      }
      // Any dead cell with exactly 3 live neighbours becomes a live cell, as if by reproduction
      else if (sum == 3) {
        newGrid(x)(y) = 1
      }
    }

    Grid(newGrid)
  }
}

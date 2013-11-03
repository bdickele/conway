package org.bdickele.conway.panel

import org.bdickele.conway._

import scala.swing._
import org.bdickele.conway.grid.{GridComputation, Pattern, Grid}
import java.util.concurrent.{Executors, TimeUnit, ExecutorService}
import scala.swing.event.MouseClicked
import scala.swing.event.MouseClicked
import java.awt.Color

/**
 * User: bdickele
 * Date: 11/2/13
 */
class PanelGrid(listener: PanelGridListener) extends BoxPanel(Orientation.Vertical) with PanelControlListener {

  var grid: Grid = Grid()

  val gridCorner = new Point(0, 0)

  val cellSize = GRID_WIDTH / Grid.size

  var currentPattern: Pattern = Pattern.Empty

  var delay = 100

  @volatile var playing = false

  var threadExecutor: ExecutorService = null

  val playJob = new Runnable {
    def run() {
      while (playing) {
        next()
        TimeUnit.MILLISECONDS.sleep(delay)
      }
    }
  }

  listenTo(this.mouse.clicks)

  reactions += {
    case MouseClicked(_, point, _, _, _) =>
      if (!playing) {
        grid.switchCell((point.y - gridCorner.y) / cellSize, (point.x - gridCorner.x) / cellSize)
        repaint()
      }
  }

  // ==================================================
  // Painting
  // ==================================================

  contents += new Component {

    override def paint(g: Graphics2D) {
      super.paint(g)
      paintGrid(g)
      paintCells(g)
    }

    def paintGrid(g: Graphics2D) {
      gridCorner.x = (WIDTH - GRID_WIDTH) / 2
      gridCorner.y = (GRID_PANEL_HEIGHT - GRID_WIDTH) / 2

      g.setColor(Color.GRAY)
      g.drawRect(gridCorner.x, gridCorner.y, GRID_WIDTH, GRID_WIDTH)

      for (i <- 1 until Grid.size) {
        // Horizontal line
        val hStartX = gridCorner.x
        val hEndX = hStartX + GRID_WIDTH
        val hStartY = gridCorner.y + cellSize * i
        val hEndY = hStartY
        g.drawLine(hStartX, hStartY, hEndX, hEndY)

        // Vertical line
        val vStartX = gridCorner.x + cellSize * i
        val vEndX = vStartX
        val vStartY = gridCorner.y
        val vEndY = vStartY + GRID_WIDTH
        g.drawLine(vStartX, vStartY, vEndX, vEndY)
      }
    }

    def paintCells(g: Graphics2D) {
      g.setColor(Color.BLACK)

      for {i <- 0 until Grid.size
           j <- 0 until Grid.size
           cellValue = grid.get(i, j)
           if cellValue != 0
           startX = gridCorner.x + (j * cellSize) + 1
           startY = gridCorner.y + (i * cellSize) + 1} {
        //println(i + "/"+j)
        g.fillRect(startX, startY, cellSize, cellSize)
      }
    }
  }

  // ==================================================
  // Listening to Grid
  // ==================================================

  def still() {
    stop()
    listener.still()
  }

  def noLiveCells() {
    stop()
    listener.noLiveCells()
  }

  // ==================================================
  // Listening to control panel
  // ==================================================

  def play() {
    playing = true
    threadExecutor = Executors.newFixedThreadPool(1)
    threadExecutor.submit(playJob)
  }

  def stop() {
    playing = false
    if (threadExecutor != null) {
      threadExecutor.shutdown()
    }
  }

  def next() {
    val previous = grid
    grid = GridComputation.computeNewGrid(previous)

    if (grid.isEmpty()) {
      noLiveCells()
    } else if (!grid.isDifferentFrom(previous)) {
      still()
    }

    repaint()
  }

  def generate(pattern: Pattern) {
    currentPattern = pattern
    grid = GridComputation.createPatternGrid(currentPattern)
    repaint()
  }

  def changeSpeed(speed: Int) {
    this.delay = speed
  }

}

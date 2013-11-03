package org.bdickele.conway.panel

import org.bdickele.conway._

import scala.swing.GridBagPanel
import java.awt.Dimension

/**
 * User: bdickele
 * Date: 11/2/13
 */
class PanelMain extends GridBagPanel with PanelGridListener {

  preferredSize = new Dimension(WIDTH, HEIGHT)
  minimumSize = new Dimension(WIDTH, HEIGHT)

  val panelGrid = new PanelGrid(this)
  val panelControl = new PanelControl(panelGrid)

  val c = new Constraints()
  c.fill = GridBagPanel.Fill.Both
  c.gridx = 0
  c.weightx = 1

  c.gridy = 1
  c.anchor = GridBagPanel.Anchor.PageEnd
  layout(panelControl) = c

  c.gridy = 0
  c.weighty = 1
  c.anchor = GridBagPanel.Anchor.PageStart
  layout(panelGrid) = c

  def still() {
    println("Still life")
    panelControl.onOff(false)
  }

  def noLiveCells() {
    println("No live cells")
    panelControl.onOff(false)
  }



}

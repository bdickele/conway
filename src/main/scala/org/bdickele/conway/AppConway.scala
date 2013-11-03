package org.bdickele.conway

import scala.swing.{MainFrame, SimpleSwingApplication}
import javax.swing.UIManager
import java.awt.Rectangle
import org.bdickele.conway.panel.PanelMain

/**
 * User: bdickele
 * Date: 11/2/13
 */
object AppConway extends SimpleSwingApplication {

  try {
    for (info <- UIManager.getInstalledLookAndFeels)
      if (info.getName == "Nimbus") UIManager.setLookAndFeel(info.getClassName)
  } catch {
    case e: Exception => println("Exception caught: " + e)
  }

  def top = new MainFrame {
    title = "Conway's game of life"
    bounds = new Rectangle(200, 100, WIDTH, HEIGHT)
    resizable = false
    contents = new PanelMain()
    visible = true
  }
}

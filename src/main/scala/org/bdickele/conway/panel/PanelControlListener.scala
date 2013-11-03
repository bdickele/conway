package org.bdickele.conway.panel

import org.bdickele.conway.grid.Pattern

/**
 * User: bdickele
 * Date: 11/2/13
 */
trait PanelControlListener {

  def play()

  def stop()

  def next()

  def generate(pattern: Pattern)

  def changeSpeed(speed: Int)

}

package org.bdickele.conway.panel

import scala.swing._
import org.bdickele.conway.grid.Pattern
import scala.swing.event.{SelectionChanged, ButtonClicked}

/**
 * User: bdickele
 * Date: 11/2/13
 */
class PanelControl(listener: PanelControlListener) extends BoxPanel(Orientation.Vertical) {

  case class ButtonSpeed(text0: String, val speed: Int) extends RadioButton(text0: String)

  val comboPattern = new ComboBox[Pattern](Pattern.patternList)
  val buttonStop = new Button("Stop")
  val buttonClean = new Button("Clean")
  val buttonPlay = new Button("Play")
  val buttonNext = new Button("Next")
  val buttonGenerate = new Button("Generate")

  val buttonSpeed1 = ButtonSpeed("1", 100)
  val buttonSpeed2 = ButtonSpeed("2", 50)
  val groupButtonSpeed = new ButtonGroup() {
    buttons ++= Set(buttonSpeed1, buttonSpeed2)
  }

  onOff(false)

  contents += new FlowPanel {
    contents += new Label("Pattern : ")
    contents += comboPattern
    contents += buttonGenerate
    contents += buttonStop
    contents += buttonClean
    contents += buttonPlay
    contents += buttonNext
  }

  contents += new FlowPanel {
    contents += new Label("Speed : ")
    contents += buttonSpeed1
    contents += buttonSpeed2

    buttonSpeed1.selected = true
  }

  listenTo(
    comboPattern.selection, buttonGenerate, buttonStop, buttonClean, buttonPlay, buttonNext,
    buttonSpeed1, buttonSpeed2)

  reactions += {
    case ButtonClicked(source: ButtonSpeed) => listener.changeSpeed(source.speed)
    case ButtonClicked(`buttonPlay`) => {
      onOff(true)
      listener.play()
    }
    case ButtonClicked(`buttonStop`) => {
      onOff(false)
      listener.stop()
    }
    case ButtonClicked(`buttonClean`) => listener.generate(Pattern.Empty)
    case ButtonClicked(`buttonNext`) => listener.next()
    case ButtonClicked(`buttonGenerate`) => listener.generate(comboPattern.selection.item)
    case SelectionChanged(`comboPattern`) => listener.generate(comboPattern.selection.item)
  }

  def onOff(b: Boolean) {
    buttonStop.enabled = b
    buttonClean.enabled = !b
    buttonPlay.enabled = !b
    buttonNext.enabled = !b
    buttonGenerate.enabled = !b
    comboPattern.enabled = !b
  }

}

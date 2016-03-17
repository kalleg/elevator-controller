package elevator;

import java.awt.event.*;
import java.io.PrintStream;

/**
 * Title:        Green Elevator
 * Description:  Green Elevator, 2G1915
 * Copyright:    Copyright (c) 2001
 * Company:      IMIT/KTH
 * @author Vlad Vlassov
 * @version 1.0
 */
/**
 * Provides an action listener for all buttons and a window listener
 * for the elevator main frame.
 */
public class ElevatorEvents extends WindowAdapter implements ActionListener {

  private java.io.PrintStream out;
  /**
   * Creates an instance ElevatorEvents listener
   */
  public ElevatorEvents(PrintStream out) {
    super();
    this.out = out;
  }
  /**
   * Invoked when a button is pressed on a floor or on an inside button panel.
   * Prints an action commands associated with the button to the output stream
   * (either standard output of a socket output stream). An action command of a floor
   * button is "b <i>f d</i>" (where "b" stands for "button", <i>f</i> is the number
   * of the floor where the button was pressed, <i>d</i> is a direction (upwards or downwards)
   * assigned with the button. An action command of a inside button is
   * "p <i>n f</i>" (where "p" stands for "panel", <i>n</i> is the number
   * of the elevator where the button was pressed, <i>f</i> is a floor number
   * assigned with the button.
   */
  public void actionPerformed(ActionEvent e) {
    out.println(e.getActionCommand());
  }
  /**
   * Invoked when the window is closing. The application exits.
   */
  public void windowClosing(WindowEvent e) {
    System.err.println("Bye Bye");
    System.exit(0);
  }
}
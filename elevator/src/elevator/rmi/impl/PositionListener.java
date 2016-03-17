package elevator.rmi.impl;

import java.rmi.RemoteException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import elevator.ElevatorGUI;
import elevator.ElevatorIO;
import elevator.Elevators;
import elevator.rmi.RemoteActionListener;
import elevator.rmi.IllegalParamException;

/**
 * Title:        Green Elevator
 * Description:  Green Elevator, 2G1915
 * Copyright:    Copyright (c) 2001
 * Company:      IMIT/KTH
 * @author Vlad Vlassov
 * @version 1.0
 */
/**
 * The helper class <code>PositionListener</code> that implements the
 * <code>java.awt.event.ActionListener</code> interface for receiving action
 * events from an the elevator timer and create an action event
 * with a current position of one elevator with a given  number or all elevators
 * (special case).
 * An object with the <code>PositionListener</code> class forwards the
 * action events via RMI to a remote listener with the
 * <code>RemoteActionListener</code> remote interface.
 * @author Vlad Vlassov, IMIT/KTH, Stockholm, Sweden
 * @version 1.0
 * @see java.awt.event.ActionListener
 * @see java.awt.event.ActionEvent
 */
public class PositionListener implements ActionListener {

  private RemoteActionListener listener;
  private int number;
  private double oldPosition, newPosition;
  private double[] oldPositions, newPositions;
  private int numberOfElevators = Elevators.numberOfElevators;
  private ElevatorIO io = ElevatorGUI.io;
  private Timer timer = ElevatorGUI.timer;
  private String eol = System.getProperty("line.separator", "\n");

    /**
     * Allocates a new <code>PositionListener</code> used to receive an event
     *  from the elevator time, include a current position of the given elevator
     *  to the event and forward the event to a remote listener.
     * @param number The integer index of the elevator (0, 1, ...)
     *    whose position to monitor.
     * @param listener The <code>RemoteActionListener</code> to which
     *    this <code>PositionListener</code> must forward action events
     *    from the timer
     */
  protected PositionListener(int number, RemoteActionListener listener)
        throws IllegalParamException {
    this.listener = listener;
    this.number = number;
    timer = ElevatorGUI.timer;
    if (timer != null) timer.addActionListener(this);
    if (number == numberOfElevators)
      newPositions = oldPositions = io.whereAre();
    else newPosition = oldPosition = io.whereIs(number + 1);
  }
  /**
   * Invoked when an inside button of the evelator is pressed. The action
   * event is forwarded to the remote listener of this
   * <code>InsideListener</code>
   */
  public void actionPerformed(ActionEvent e) {
    if (number == numberOfElevators) {
      try {
        newPositions = io.whereAre();
      } catch (IllegalParamException ex) {;} // should not be
      StringBuffer position = new StringBuffer();
      boolean first = true;
      for (int i = 0; i < newPositions.length; i++) {
        if (newPositions[i] != oldPositions[i]) {
          oldPositions[i] = newPositions[i];
          if (first) {
            position.append("f " + (i + 1) + " "+ String.valueOf(newPositions[i]));
            first = false;
          } else
            position.append(eol + "f " + (i + 1) + " "+ String.valueOf(newPositions[i]));
        }
      }
      if (position.length() > 0) {
        try {
          listener.actionPerformed(new ActionEvent(e.getSource(), e.getID(), position.toString()));
        } catch (RemoteException ex) {
          removePositionListener();
        }
      }
    } else {
      try {
        newPosition = io.whereIs(number + 1);
      } catch (IllegalParamException ex) {;} // should not be
      if (newPosition != oldPosition) {
        oldPosition = newPosition;
        String position = "f " + (number + 1) + " "+ String.valueOf(newPosition);
        try {
          listener.actionPerformed(new ActionEvent(e.getSource(), e.getID(), position));
        } catch (RemoteException ex) {
          removePositionListener();
        }
      }
    }
  }
  private void removePositionListener() {
    System.err.println("Got RemoteException while calling back to a position listener");
    System.err.println("Removing the listener");
    timer.removeActionListener(this);
    listener = null; // hopefully will be gc-ed also
  }
}
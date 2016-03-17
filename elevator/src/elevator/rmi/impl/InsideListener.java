package elevator.rmi.impl;

import java.rmi.RemoteException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import elevator.ElevatorGUI;
import elevator.Elevators;
import elevator.rmi.RemoteActionListener;

/**
 * Title:        Green Elevator
 * Description:  Green Elevator, 2G1915
 * Copyright:    Copyright (c) 2001
 * Company:      IMIT/KTH
 * @author Vlad Vlassov
 * @version 1.0
 */
/**
 * The helper class <code>InsideListener</code> that implements the
 * <code>java.awt.event.ActionListener</code> interface for receiving action
 * events from an inside button panel of one elevator with a given  number or
 * from inside panels of all elevators.
 * An object with the <code>InsideListener</code> class forwards the
 * action events via RMI to a remote listener with the
 * <code>RemoteActionListener</code> remote interface.
 * @author Vlad Vlassov, IMIT/KTH, Stockholm, Sweden
 * @version 1.0
 * @see java.awt.event.ActionListener
 * @see java.awt.event.ActionEvent
 */
public class InsideListener implements ActionListener {

  private RemoteActionListener listener;
  private int number;
  private JButton[][] button = ElevatorGUI.insideButton;
  private int numberOfElevators = Elevators.numberOfElevators;
    /**
     * Allocates a new <code>InsideListener</code> used to receive events
     *  from an inside button panel of the elevator and to forward the events to a
     *  remote listener.
     * @param number The integer relative number of the elevator (0, 1, ...)
     *    to whose inside buttons
     *    this <code>InsideButtonsListener</code> is added as an action listener.
     * @param listener The <code>RemoteActionListener</code> to which
     *    this <code>InsideButtonsListener</code> must forward action events
     *    from the buttons
     */
  protected InsideListener(int number, RemoteActionListener listener) {
    this.listener = listener;
    this.number = number;
    if (number == numberOfElevators) {
      if (button != null) {
        for (int i = numberOfElevators - 1; i >= 0; i--) {
          if (button[i] != null) {
            for (int j = button[i].length - 1; j >= 0; j--) {
              if (button[i][j] != null) button[i][j].addActionListener(this);
            }
          }
        }
      }
    } else {
      if (button != null && button[number] != null)
        for (int i = button[number].length - 1; i >= 0; i--)
          if (button[number][i] != null)
            button[number][i].addActionListener(this);
    }
  }
  /**
   * Invoked when an inside button of the evelator is pressed. The action
   * event is forwarded to the remote listener of this
   * <code>InsideListener</code>
   */
  public void actionPerformed(ActionEvent e) {
    try {
      listener.actionPerformed(e);
    } catch (RemoteException ex) {
      System.err.println("Got RemoteException while calling back to a listener");
      System.err.println("Removing the listener");
      if (number == numberOfElevators)  { // special case: one listener for all buttons
        for (int i = numberOfElevators - 1; i >= 0; i--)
          for (int j = button[i].length - 1; j >= 0; j--)
            button[i][j].removeActionListener(this);
      } else {
        for (int i = button[number].length - 1; i >= 0; i--)
          button[number][i].removeActionListener(this);
      }
      listener = null; // hopefully will be gc-ed also
    }
  }
}